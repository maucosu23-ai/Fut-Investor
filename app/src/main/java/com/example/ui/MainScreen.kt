package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.PlayerItem
import com.example.ui.components.AiAdvisorModal
import com.example.ui.components.FutTopBar
import com.example.ui.components.PlayerDetailModal
import com.example.ui.screens.ArbitrageScreen
import com.example.ui.screens.CalculatorAndPortfolioScreen
import com.example.ui.screens.CompareScreen
import com.example.ui.screens.NewsAndLeaksScreen
import com.example.ui.theme.FutGold
import com.example.ui.theme.FutSurface
import com.example.ui.theme.FutSurfaceVariant
import com.example.ui.theme.FutTextPrimary
import com.example.ui.theme.FutTextSecondary

@Composable
fun MainScreen(viewModel: TradingViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var detailPlayer by remember { mutableStateOf<PlayerItem?>(null) }
    var showAiModal by remember { mutableStateOf(false) }

    // Show snackbar events
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissSnackbar()
        }
    }

    Scaffold(
        topBar = {
            FutTopBar(
                platform = uiState.selectedPlatform,
                onPlatformChange = { viewModel.setPlatform(it) },
                isRefreshing = uiState.isRefreshing,
                onRefreshClick = { viewModel.refreshMarketPrices(silent = false) },
                autoRefreshEnabled = uiState.autoRefreshEnabled,
                onToggleAutoRefresh = { viewModel.toggleAutoRefresh() },
                lastSyncTime = uiState.lastSyncTime,
                futbinLatency = uiState.futbinLatencyMs,
                futggLatency = uiState.futggLatencyMs
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = FutSurface,
                modifier = Modifier.testTag("main_navigation_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.COMPARE,
                    onClick = { viewModel.selectTab(MainTab.COMPARE) },
                    icon = { Icon(Icons.Default.CompareArrows, contentDescription = "Comparador") },
                    label = { Text("Comparador", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = FutGold,
                        indicatorColor = FutGold,
                        unselectedIconColor = FutTextSecondary,
                        unselectedTextColor = FutTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_compare")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.ARBITRAGE,
                    onClick = { viewModel.selectTab(MainTab.ARBITRAGE) },
                    icon = { Icon(Icons.Default.AutoGraph, contentDescription = "Arbitraje Auto") },
                    label = { Text("Arbitraje", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = FutGold,
                        indicatorColor = FutGold,
                        unselectedIconColor = FutTextSecondary,
                        unselectedTextColor = FutTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_arbitrage")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.EXPERTS,
                    onClick = { viewModel.selectTab(MainTab.EXPERTS) },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Expertos") },
                    label = { Text("Expertos", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = FutGold,
                        indicatorColor = FutGold,
                        unselectedIconColor = FutTextSecondary,
                        unselectedTextColor = FutTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_experts")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.NEWS_FC27,
                    onClick = { viewModel.selectTab(MainTab.NEWS_FC27) },
                    icon = { Icon(Icons.Default.Campaign, contentDescription = "Noticias & Leaks") },
                    label = { Text("FC 27 Leaks", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = FutGold,
                        indicatorColor = FutGold,
                        unselectedIconColor = FutTextSecondary,
                        unselectedTextColor = FutTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_news")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == MainTab.CALCULATOR_PORTFOLIO,
                    onClick = { viewModel.selectTab(MainTab.CALCULATOR_PORTFOLIO) },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculadora & Cartera") },
                    label = { Text("Calculadora", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = FutGold,
                        indicatorColor = FutGold,
                        unselectedIconColor = FutTextSecondary,
                        unselectedTextColor = FutTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_calculator")
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                MainTab.COMPARE -> CompareScreen(
                    players = uiState.filteredPlayers,
                    searchQuery = uiState.searchQuery,
                    onSearchChange = { viewModel.setSearchQuery(it) },
                    selectedCategory = uiState.selectedCategory,
                    onCategoryChange = { viewModel.setSelectedCategory(it) },
                    platform = uiState.selectedPlatform,
                    watchlistPlayerIds = uiState.watchlist.map { it.playerId }.toSet(),
                    onWatchlistToggle = { viewModel.toggleWatchlist(it) },
                    onPlayerClick = { detailPlayer = it },
                    onAiConsultClick = {
                        viewModel.requestAiAnalysisForPlayer(it)
                        showAiModal = true
                    },
                    onCalcClick = { player ->
                        viewModel.updateCalcInputs(
                            player.getFutbinPrice(uiState.selectedPlatform).toString(),
                            player.getFutggPrice(uiState.selectedPlatform).toString()
                        )
                        viewModel.selectTab(MainTab.CALCULATOR_PORTFOLIO)
                    },
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = { viewModel.refreshMarketPrices(silent = false) }
                )

                MainTab.ARBITRAGE -> ArbitrageScreen(
                    opportunities = uiState.arbitrageOpportunities,
                    selectedTypeFilter = uiState.selectedArbitrageFilter,
                    onTypeFilterChange = { viewModel.setArbitrageFilter(it) },
                    budgetFilterCoins = uiState.budgetFilterCoins,
                    onBudgetFilterChange = { viewModel.setBudgetFilterCoins(it) },
                    platform = uiState.selectedPlatform,
                    onAiConsultClick = {
                        viewModel.requestAiAnalysisForPlayer(it)
                        showAiModal = true
                    },
                    onAddToPortfolio = { player, buy, sell, chem ->
                        viewModel.addPortfolioCard(player, buy, sell, chem)
                    },
                    onPlayerClick = { detailPlayer = it }
                )

                MainTab.EXPERTS -> com.example.ui.screens.ExpertsScreen(
                    recommendations = uiState.filteredExpertRecommendations,
                    expertProfiles = uiState.expertProfiles,
                    searchQuery = uiState.expertSearchQuery,
                    onSearchChange = { viewModel.setExpertSearchQuery(it) },
                    selectedTag = uiState.selectedExpertTag,
                    onTagSelect = { viewModel.setSelectedExpertTag(it) },
                    selectedProfile = uiState.selectedExpertProfile,
                    onProfileSelect = { viewModel.setSelectedExpertProfile(it) },
                    selectedConsensus = uiState.selectedConsensus,
                    onViewConsensus = { viewModel.viewConsensusForPlayer(it) },
                    onDismissConsensus = { viewModel.dismissConsensus() },
                    onUpvote = { viewModel.upvoteExpertRecommendation(it) },
                    onAddToPortfolio = { player, buy, sell, chem ->
                        viewModel.addPortfolioCard(player, buy, sell, chem)
                        viewModel.selectTab(MainTab.CALCULATOR_PORTFOLIO)
                    },
                    onPlayerClick = { detailPlayer = it }
                )

                MainTab.NEWS_FC27 -> NewsAndLeaksScreen(
                    newsList = uiState.newsList,
                    aiAnalysis = uiState.aiAnalysis,
                    isAiAnalyzing = uiState.isAiAnalyzing,
                    onAiQuerySubmit = { query ->
                        viewModel.requestAiCustomQuery(query)
                    },
                    onTrackTradeInPortfolio = { analysis ->
                        val matchedPlayer = uiState.players.find {
                            it.name.contains(analysis.queryOrPlayer, ignoreCase = true) ||
                                analysis.queryOrPlayer.contains(it.name, ignoreCase = true)
                        } ?: uiState.players.first()

                        viewModel.addPortfolioCard(
                            matchedPlayer,
                            analysis.targetBuyPrice,
                            analysis.targetSellPrice,
                            "Meta Sombra/Cazador"
                        )
                        viewModel.selectTab(MainTab.CALCULATOR_PORTFOLIO)
                    },
                    onAffectedPlayerClick = { playerName ->
                        val matched = uiState.players.find { it.name.contains(playerName, ignoreCase = true) }
                        if (matched != null) {
                            detailPlayer = matched
                        } else {
                            viewModel.setSearchQuery(playerName)
                            viewModel.selectTab(MainTab.COMPARE)
                        }
                    }
                )

                MainTab.CALCULATOR_PORTFOLIO -> CalculatorAndPortfolioScreen(
                    calcBuyInput = uiState.calcBuyInput,
                    calcSellInput = uiState.calcSellInput,
                    onCalcInputsChange = { buy, sell -> viewModel.updateCalcInputs(buy, sell) },
                    portfolioList = uiState.portfolio,
                    watchlist = uiState.watchlist,
                    onMarkSold = { id, soldPrice -> viewModel.markPortfolioSold(id, soldPrice) }
                )
            }
        }
    }

    // Modal: Detailed Player Analytics & Chem Styles
    if (detailPlayer != null) {
        PlayerDetailModal(
            player = detailPlayer,
            platform = uiState.selectedPlatform,
            onDismiss = { detailPlayer = null },
            onAiConsultClick = { player ->
                viewModel.requestAiAnalysisForPlayer(player)
                showAiModal = true
            },
            onAddToPortfolio = { player, buy, sell, chem ->
                viewModel.addPortfolioCard(player, buy, sell, chem)
            }
        )
    }

    // Modal: AI Trading Advisor
    if (showAiModal) {
        AiAdvisorModal(
            analysis = uiState.aiAnalysis,
            isLoading = uiState.isAiAnalyzing,
            onDismiss = { showAiModal = false },
            onTrackTradeClick = { analysis ->
                val matchedPlayer = uiState.players.find {
                    it.name.contains(analysis.queryOrPlayer, ignoreCase = true) ||
                        analysis.queryOrPlayer.contains(it.name, ignoreCase = true)
                } ?: uiState.players.first()

                viewModel.addPortfolioCard(
                    matchedPlayer,
                    analysis.targetBuyPrice,
                    analysis.targetSellPrice,
                    "Recomendado por IA"
                )
                showAiModal = false
                viewModel.selectTab(MainTab.CALCULATOR_PORTFOLIO)
            }
        )
    }
}
