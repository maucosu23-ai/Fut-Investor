package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.PortfolioEntity
import com.example.data.local.entity.WatchlistEntity
import com.example.data.model.AiAnalysisResult
import com.example.data.model.ArbitrageOpportunity
import com.example.data.model.ArbitrageType
import com.example.data.model.Fc27NewsItem
import com.example.data.model.Platform
import com.example.data.model.PlayerItem
import com.example.data.repository.ArbitrageEngine
import com.example.data.repository.MarketRepository
import com.example.network.FutPriceApiService
import com.example.network.GeminiTradingService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class MainTab(val title: String, val iconName: String) {
    COMPARE("Comparador", "compare_arrows"),
    ARBITRAGE("Arbitraje Auto", "auto_graph"),
    EXPERTS("Expertos", "military_tech"),
    NEWS_FC27("Noticias & Leaks", "campaign"),
    CALCULATOR_PORTFOLIO("Calculadora & Cartera", "calculate")
}

data class TradingUiState(
    val currentTab: MainTab = MainTab.COMPARE,
    val players: List<PlayerItem> = emptyList(),
    val filteredPlayers: List<PlayerItem> = emptyList(),
    val searchQuery: String = "",
    val selectedPlatform: Platform = Platform.CONSOLE,
    val selectedCategory: String = "Todos",
    val selectedPlayer: PlayerItem? = null,
    val arbitrageOpportunities: List<ArbitrageOpportunity> = emptyList(),
    val selectedArbitrageFilter: ArbitrageType? = null,
    val budgetFilterCoins: Int = 10000000, // No limit by default
    val isRefreshing: Boolean = false,
    val lastSyncTime: String = "En Vivo",
    val futbinLatencyMs: Long = 64,
    val futggLatencyMs: Long = 78,
    val autoRefreshEnabled: Boolean = true,
    val newsList: List<Fc27NewsItem> = emptyList(),
    val selectedNews: Fc27NewsItem? = null,
    val aiAnalysis: AiAnalysisResult? = null,
    val isAiAnalyzing: Boolean = false,
    val aiQueryInput: String = "",
    val calcBuyInput: String = "50000",
    val calcSellInput: String = "62000",
    val watchlist: List<WatchlistEntity> = emptyList(),
    val portfolio: List<PortfolioEntity> = emptyList(),
    val snackbarMessage: String? = null,
    val expertProfiles: List<com.example.data.model.ExpertProfile> = com.example.data.repository.ExpertRepository.experts,
    val expertRecommendations: List<com.example.data.model.ExpertRecommendation> = emptyList(),
    val filteredExpertRecommendations: List<com.example.data.model.ExpertRecommendation> = emptyList(),
    val expertSearchQuery: String = "",
    val selectedExpertTag: String? = null,
    val selectedExpertProfile: com.example.data.model.ExpertProfile? = null,
    val selectedConsensus: com.example.data.model.ExpertConsensus? = null
)

class TradingViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val watchlistDao = db.watchlistDao()
    private val portfolioDao = db.portfolioDao()

    private val _uiState = MutableStateFlow(TradingUiState())
    val uiState: StateFlow<TradingUiState> = _uiState.asStateFlow()

    private var autoRefreshJob: Job? = null

    init {
        // Observe market players
        viewModelScope.launch {
            MarketRepository.players.collectLatest { playerList ->
                _uiState.update { state ->
                    val filtered = applyPlayerFilters(playerList, state.searchQuery, state.selectedCategory)
                    val arbs = ArbitrageEngine.scanOpportunities(
                        players = playerList,
                        platform = state.selectedPlatform,
                        budgetLimit = state.budgetFilterCoins
                    )
                    val recs = com.example.data.repository.ExpertRepository.getRecommendations(playerList)
                    val filteredRecs = applyExpertFilters(
                        list = recs,
                        query = state.expertSearchQuery,
                        tag = state.selectedExpertTag,
                        profile = state.selectedExpertProfile
                    )
                    state.copy(
                        players = playerList,
                        filteredPlayers = filtered,
                        arbitrageOpportunities = filterArbs(arbs, state.selectedArbitrageFilter),
                        expertRecommendations = recs,
                        filteredExpertRecommendations = filteredRecs
                    )
                }
            }
        }

        // Observe news feed
        viewModelScope.launch {
            MarketRepository.newsFeed.collectLatest { news ->
                _uiState.update { it.copy(newsList = news) }
            }
        }

        // Observe watchlist
        viewModelScope.launch {
            watchlistDao.getAllWatchlist().collectLatest { list ->
                _uiState.update { it.copy(watchlist = list) }
            }
        }

        // Observe portfolio
        viewModelScope.launch {
            portfolioDao.getAllPortfolio().collectLatest { list ->
                _uiState.update { it.copy(portfolio = list) }
            }
        }

        // Initial default AI analysis with top market recommendation
        viewModelScope.launch {
            delay(500)
            requestAiAnalysisForPlayer(MarketRepository.players.value.first())
        }

        // Start background live market polling
        startAutoRefreshTicker()
    }

    fun selectTab(tab: MainTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun setPlatform(platform: Platform) {
        MarketRepository.setPlatform(platform)
        _uiState.update { state ->
            val arbs = ArbitrageEngine.scanOpportunities(
                players = state.players,
                platform = platform,
                budgetLimit = state.budgetFilterCoins
            )
            state.copy(
                selectedPlatform = platform,
                arbitrageOpportunities = filterArbs(arbs, state.selectedArbitrageFilter)
            )
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { state ->
            val filtered = applyPlayerFilters(state.players, query, state.selectedCategory)
            state.copy(searchQuery = query, filteredPlayers = filtered)
        }
    }

    fun setSelectedCategory(category: String) {
        _uiState.update { state ->
            val filtered = applyPlayerFilters(state.players, state.searchQuery, category)
            state.copy(selectedCategory = category, filteredPlayers = filtered)
        }
    }

    fun selectPlayer(player: PlayerItem?) {
        _uiState.update { it.copy(selectedPlayer = player) }
    }

    fun selectNews(news: Fc27NewsItem?) {
        _uiState.update { it.copy(selectedNews = news) }
    }

    fun setArbitrageFilter(type: ArbitrageType?) {
        _uiState.update { state ->
            val allArbs = ArbitrageEngine.scanOpportunities(
                players = state.players,
                platform = state.selectedPlatform,
                budgetLimit = state.budgetFilterCoins
            )
            state.copy(
                selectedArbitrageFilter = type,
                arbitrageOpportunities = filterArbs(allArbs, type)
            )
        }
    }

    fun setBudgetFilterCoins(maxCoins: Int) {
        _uiState.update { state ->
            val allArbs = ArbitrageEngine.scanOpportunities(
                players = state.players,
                platform = state.selectedPlatform,
                budgetLimit = maxCoins
            )
            state.copy(
                budgetFilterCoins = maxCoins,
                arbitrageOpportunities = filterArbs(allArbs, state.selectedArbitrageFilter)
            )
        }
    }

    fun toggleAutoRefresh() {
        val next = !_uiState.value.autoRefreshEnabled
        _uiState.update { it.copy(autoRefreshEnabled = next) }
        if (next) {
            startAutoRefreshTicker()
            showSnackbar("Actualización en tiempo real activada (cada 15s)")
        } else {
            autoRefreshJob?.cancel()
            showSnackbar("Actualización automática pausada")
        }
    }

    private fun startAutoRefreshTicker() {
        autoRefreshJob?.cancel()
        autoRefreshJob = viewModelScope.launch {
            while (true) {
                delay(15000) // Poll every 15 seconds
                if (_uiState.value.autoRefreshEnabled) {
                    refreshMarketPrices(silent = true)
                }
            }
        }
    }

    fun refreshMarketPrices(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) {
                _uiState.update { it.copy(isRefreshing = true) }
            }
            try {
                // Query live latency and prices from Futbin & FUT.GG
                val sample = _uiState.value.players.firstOrNull()
                val futbinRes = FutPriceApiService.fetchFutbinPrice(
                    sample?.id ?: "yamal_88_rttk",
                    sample?.name ?: "Lamine Yamal",
                    sample?.futbinPriceConsole ?: 620000
                )
                val futggRes = FutPriceApiService.fetchFutGgPrice(
                    sample?.id ?: "yamal_88_rttk",
                    sample?.name ?: "Lamine Yamal",
                    sample?.futbinPriceConsole ?: 620000
                )

                MarketRepository.refreshLiveQuotes()

                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val nowStr = timeFormat.format(Date())

                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        lastSyncTime = nowStr,
                        futbinLatencyMs = futbinRes.latencyMs,
                        futggLatencyMs = futggRes.latencyMs
                    )
                }
                if (!silent) {
                    showSnackbar("Precios Futbin y FUT.GG actualizados con éxito")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isRefreshing = false) }
                if (!silent) {
                    showSnackbar("Error al conectar con los servidores de precios")
                }
            }
        }
    }

    fun requestAiAnalysisForPlayer(player: PlayerItem) {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiAnalyzing = true, selectedPlayer = player) }
            val analysis = GeminiTradingService.analyzeTradingStrategy(player, null)
            _uiState.update { it.copy(aiAnalysis = analysis, isAiAnalyzing = false) }
        }
    }

    fun requestAiCustomQuery(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isAiAnalyzing = true, aiQueryInput = "") }
            val analysis = GeminiTradingService.analyzeTradingStrategy(null, query)
            _uiState.update { it.copy(aiAnalysis = analysis, isAiAnalyzing = false) }
        }
    }

    fun toggleWatchlist(player: PlayerItem) {
        viewModelScope.launch {
            val exists = watchlistDao.isPlayerInWatchlist(player.id)
            if (exists) {
                watchlistDao.deleteById(player.id)
                showSnackbar("${player.name} eliminado del radar")
            } else {
                val item = WatchlistEntity(
                    playerId = player.id,
                    playerName = player.name,
                    rating = player.rating,
                    position = player.position,
                    cardType = player.cardType,
                    alertPriceBelow = (player.futbinPriceConsole * 0.94f).toInt(),
                    targetSellPrice = (player.futggPriceConsole * 1.08f).toInt(),
                    initialFutbinPrice = player.futbinPriceConsole,
                    initialFutggPrice = player.futggPriceConsole
                )
                watchlistDao.insert(item)
                showSnackbar("${player.name} guardado en el radar de precios")
            }
        }
    }

    fun addPortfolioCard(player: PlayerItem, buyPrice: Int, targetSell: Int, chemStyle: String) {
        viewModelScope.launch {
            val entity = PortfolioEntity(
                playerId = player.id,
                playerName = player.name,
                rating = player.rating,
                position = player.position,
                cardType = player.cardType,
                buyPrice = buyPrice,
                targetSellPrice = targetSell,
                chemStyle = chemStyle
            )
            portfolioDao.insert(entity)
            showSnackbar("Inversión en ${player.name} registrada en la cartera")
        }
    }

    fun markPortfolioSold(id: Long, soldPrice: Int) {
        viewModelScope.launch {
            portfolioDao.markAsSold(id, soldPrice)
            showSnackbar("Venta confirmada. ¡Beneficios calculados!")
        }
    }

    fun updateCalcInputs(buy: String, sell: String) {
        _uiState.update { it.copy(calcBuyInput = buy, calcSellInput = sell) }
    }

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun dismissSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun setExpertSearchQuery(query: String) {
        _uiState.update { state ->
            val filtered = applyExpertFilters(
                list = state.expertRecommendations,
                query = query,
                tag = state.selectedExpertTag,
                profile = state.selectedExpertProfile
            )
            state.copy(expertSearchQuery = query, filteredExpertRecommendations = filtered)
        }
    }

    fun setSelectedExpertTag(tag: String?) {
        _uiState.update { state ->
            val filtered = applyExpertFilters(
                list = state.expertRecommendations,
                query = state.expertSearchQuery,
                tag = tag,
                profile = state.selectedExpertProfile
            )
            state.copy(selectedExpertTag = tag, filteredExpertRecommendations = filtered)
        }
    }

    fun setSelectedExpertProfile(profile: com.example.data.model.ExpertProfile?) {
        _uiState.update { state ->
            val filtered = applyExpertFilters(
                list = state.expertRecommendations,
                query = state.expertSearchQuery,
                tag = state.selectedExpertTag,
                profile = profile
            )
            state.copy(selectedExpertProfile = profile, filteredExpertRecommendations = filtered)
        }
    }

    fun viewConsensusForPlayer(player: PlayerItem) {
        val consensus = com.example.data.repository.ExpertRepository.buildConsensus(player)
        _uiState.update { it.copy(selectedConsensus = consensus) }
    }

    fun dismissConsensus() {
        _uiState.update { it.copy(selectedConsensus = null) }
    }

    fun upvoteExpertRecommendation(recId: String) {
        _uiState.update { state ->
            val updated = state.expertRecommendations.map { rec ->
                if (rec.id == recId) rec.copy(upvotes = rec.upvotes + 1) else rec
            }
            val filtered = applyExpertFilters(
                list = updated,
                query = state.expertSearchQuery,
                tag = state.selectedExpertTag,
                profile = state.selectedExpertProfile
            )
            state.copy(
                expertRecommendations = updated,
                filteredExpertRecommendations = filtered
            )
        }
        showSnackbar("Voto registrado como recomendación de valor")
    }

    private fun applyExpertFilters(
        list: List<com.example.data.model.ExpertRecommendation>,
        query: String,
        tag: String?,
        profile: com.example.data.model.ExpertProfile?
    ): List<com.example.data.model.ExpertRecommendation> {
        return list.filter { rec ->
            val matchesQuery = query.isEmpty() ||
                rec.player.name.contains(query, ignoreCase = true) ||
                rec.player.club.contains(query, ignoreCase = true) ||
                rec.expert.name.contains(query, ignoreCase = true) ||
                rec.expert.specialty.contains(query, ignoreCase = true) ||
                rec.thesis.contains(query, ignoreCase = true) ||
                rec.tags.any { it.contains(query, ignoreCase = true) }

            val matchesTag = tag == null || rec.tags.contains(tag)
            val matchesProfile = profile == null || rec.expert.id == profile.id

            matchesQuery && matchesTag && matchesProfile
        }
    }

    private fun applyPlayerFilters(
        list: List<PlayerItem>,
        query: String,
        category: String
    ): List<PlayerItem> {
        return list.filter { player ->
            val matchesQuery = query.isEmpty() ||
                player.name.contains(query, ignoreCase = true) ||
                player.club.contains(query, ignoreCase = true) ||
                player.league.contains(query, ignoreCase = true) ||
                player.position.contains(query, ignoreCase = true)

            val matchesCategory = when (category) {
                "Meta Oro" -> !player.isFodder && player.cardType.contains("Oro")
                "Promos & TOTW" -> player.cardType != "Oro Único" && !player.isFodder
                "Medias Fodder (84-90)" -> player.isFodder
                else -> true
            }

            matchesQuery && matchesCategory
        }
    }

    private fun filterArbs(
        arbs: List<ArbitrageOpportunity>,
        filter: ArbitrageType?
    ): List<ArbitrageOpportunity> {
        return if (filter == null) arbs else arbs.filter { it.type == filter }
    }
}
