package com.example.data.repository

import com.example.data.model.ArbitrageOpportunity
import com.example.data.model.ArbitrageType
import com.example.data.model.Fc27NewsItem
import com.example.data.model.NewsCategory
import com.example.data.model.Platform
import com.example.data.model.PlayerItem
import com.example.data.model.RiskLevel
import com.example.data.model.TradingSignal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

object MarketRepository {

    private val initialPlayers = listOf(
        PlayerItem(
            id = "yamal_88_rttk",
            name = "Lamine Yamal",
            rating = 88,
            position = "ED / MD",
            club = "FC Barcelona",
            league = "LaLiga EA Sports",
            nation = "España",
            cardType = "RTTK Champions",
            futbinPriceConsole = 620000,
            futbinPricePc = 680000,
            futggPriceConsole = 675000,
            futggPricePc = 720000,
            lowestBinConsole = 615000,
            lowestBinPc = 665000,
            dailyMin = 590000,
            dailyMax = 690000,
            priceHistory24h = listOf(595000, 605000, 618000, 610000, 630000, 650000, 675000),
            priceChange24h = 8.4f,
            chemStyleShadowPriceConsole = 635000,
            chemStyleHunterPriceConsole = 660000,
            isFodder = false,
            promoUpcoming = true,
            leakNotes = "Clasificación Champions casi asegurada. Subirá a 89 de media tras el próximo partido."
        ),
        PlayerItem(
            id = "mbappe_91_gold",
            name = "Kylian Mbappé",
            rating = 91,
            position = "DC / EI",
            club = "Real Madrid",
            league = "LaLiga EA Sports",
            nation = "Francia",
            cardType = "Oro Único",
            futbinPriceConsole = 2180000,
            futbinPricePc = 2390000,
            futggPriceConsole = 2270000,
            futggPricePc = 2460000,
            lowestBinConsole = 2170000,
            lowestBinPc = 2375000,
            dailyMin = 2120000,
            dailyMax = 2310000,
            priceHistory24h = listOf(2250000, 2220000, 2190000, 2170000, 2185000, 2200000, 2180000),
            priceChange24h = -2.1f,
            chemStyleShadowPriceConsole = 2190000,
            chemStyleHunterPriceConsole = 2240000,
            isFodder = false,
            promoUpcoming = false,
            leakNotes = "Meta indiscutible. Rebota fuerte cada jueves tras recompensas de Division Rivals."
        ),
        PlayerItem(
            id = "haaland_91_gold",
            name = "Erling Haaland",
            rating = 91,
            position = "DC",
            club = "Manchester City",
            league = "Premier League",
            nation = "Noruega",
            cardType = "Oro Único",
            futbinPriceConsole = 175000,
            futbinPricePc = 198000,
            futggPriceConsole = 192000,
            futggPricePc = 210000,
            lowestBinConsole = 172000,
            lowestBinPc = 194000,
            dailyMin = 168000,
            dailyMax = 198000,
            priceHistory24h = listOf(169000, 172000, 174000, 180000, 185000, 190000, 175000),
            priceChange24h = 4.2f,
            chemStyleShadowPriceConsole = 177000,
            chemStyleHunterPriceConsole = 192000,
            isFodder = false,
            promoUpcoming = true,
            leakNotes = "Candidato firme a TOTW tras hat-trick en liga. Si entra, su carta oro saldrá de sobres (Out of Packs)."
        ),
        PlayerItem(
            id = "vinicius_90_gold",
            name = "Vinícius Jr.",
            rating = 90,
            position = "EI",
            club = "Real Madrid",
            league = "LaLiga EA Sports",
            nation = "Brasil",
            cardType = "Oro Único",
            futbinPriceConsole = 845000,
            futbinPricePc = 910000,
            futggPriceConsole = 885000,
            futggPricePc = 945000,
            lowestBinConsole = 838000,
            lowestBinPc = 902000,
            dailyMin = 820000,
            dailyMax = 890000,
            priceHistory24h = listOf(860000, 850000, 842000, 839000, 848000, 860000, 845000),
            priceChange24h = 1.3f,
            chemStyleShadowPriceConsole = 848000,
            chemStyleHunterPriceConsole = 878000,
            isFodder = false,
            promoUpcoming = false,
            leakNotes = "Fluctuación regular entre viernes noche y domingo tarde por Champions."
        ),
        PlayerItem(
            id = "bellingham_90_gold",
            name = "Jude Bellingham",
            rating = 90,
            position = "MCO / MC",
            club = "Real Madrid",
            league = "LaLiga EA Sports",
            nation = "Inglaterra",
            cardType = "Oro Único",
            futbinPriceConsole = 395000,
            futbinPricePc = 430000,
            futggPriceConsole = 422000,
            futggPricePc = 455000,
            lowestBinConsole = 390000,
            lowestBinPc = 425000,
            dailyMin = 380000,
            dailyMax = 435000,
            priceHistory24h = listOf(410000, 405000, 395000, 392000, 402000, 415000, 395000),
            priceChange24h = -3.5f,
            chemStyleShadowPriceConsole = 418000,
            chemStyleHunterPriceConsole = 422000,
            isFodder = false,
            promoUpcoming = true,
            leakNotes = "Filtrada nueva versión Trailblazers con PlayStyle+ Pase Incisivo."
        ),
        PlayerItem(
            id = "saliba_87_totw",
            name = "William Saliba",
            rating = 88,
            position = "DFC",
            club = "Arsenal",
            league = "Premier League",
            nation = "Francia",
            cardType = "TOTW En Forma",
            futbinPriceConsole = 285000,
            futbinPricePc = 315000,
            futggPriceConsole = 310000,
            futggPricePc = 338000,
            lowestBinConsole = 280000,
            lowestBinPc = 310000,
            dailyMin = 270000,
            dailyMax = 320000,
            priceHistory24h = listOf(275000, 280000, 288000, 295000, 302000, 310000, 285000),
            priceChange24h = 5.2f,
            chemStyleShadowPriceConsole = 312000,
            chemStyleHunterPriceConsole = 288000,
            isFodder = false,
            promoUpcoming = false,
            leakNotes = "Defensa meta número 1 de Premier. Margen brutal en cartas con Estilo Sombra."
        ),
        PlayerItem(
            id = "valverde_88_gold",
            name = "Federico Valverde",
            rating = 88,
            position = "MC / MD",
            club = "Real Madrid",
            league = "LaLiga EA Sports",
            nation = "Uruguay",
            cardType = "Oro Único",
            futbinPriceConsole = 245000,
            futbinPricePc = 272000,
            futggPriceConsole = 264000,
            futggPricePc = 289000,
            lowestBinConsole = 240000,
            lowestBinPc = 268000,
            dailyMin = 238000,
            dailyMax = 275000,
            priceHistory24h = listOf(250000, 246000, 242000, 245000, 255000, 260000, 245000),
            priceChange24h = 2.0f,
            chemStyleShadowPriceConsole = 265000,
            chemStyleHunterPriceConsole = 262000,
            isFodder = false,
            promoUpcoming = true,
            leakNotes = "Rumor de evolución exclusiva para centrocampistas todoterreno de LaLiga."
        ),
        PlayerItem(
            id = "vandijk_89_gold",
            name = "Virgil van Dijk",
            rating = 89,
            position = "DFC",
            club = "Liverpool",
            league = "Premier League",
            nation = "Países Bajos",
            cardType = "Oro Único",
            futbinPriceConsole = 195000,
            futbinPricePc = 220000,
            futggPriceConsole = 212000,
            futggPricePc = 236000,
            lowestBinConsole = 192000,
            lowestBinPc = 216000,
            dailyMin = 188000,
            dailyMax = 224000,
            priceHistory24h = listOf(205000, 200000, 194000, 192000, 198000, 210000, 195000),
            priceChange24h = -1.8f,
            chemStyleShadowPriceConsole = 222000,
            chemStyleHunterPriceConsole = 198000,
            isFodder = false,
            promoUpcoming = false,
            leakNotes = "El muro de FC 26/27. Comprar con Sombra por debajo de 198k deja más de 12k limpios."
        ),
        PlayerItem(
            id = "theo_87_gold",
            name = "Theo Hernández",
            rating = 87,
            position = "LI",
            club = "Milan",
            league = "Serie A Enilive",
            nation = "Francia",
            cardType = "Oro Único",
            futbinPriceConsole = 128000,
            futbinPricePc = 148000,
            futggPriceConsole = 142000,
            futggPricePc = 160000,
            lowestBinConsole = 125000,
            lowestBinPc = 144000,
            dailyMin = 122000,
            dailyMax = 148000,
            priceHistory24h = listOf(132000, 130000, 126000, 125000, 134000, 140000, 128000),
            priceChange24h = 3.6f,
            chemStyleShadowPriceConsole = 144000,
            chemStyleHunterPriceConsole = 138000,
            isFodder = false,
            promoUpcoming = false,
            leakNotes = "Lateral más cotizado para FUT Champions. Sniping muy rentable en filtros 59 min."
        ),
        PlayerItem(
            id = "fodder_88_kane",
            name = "Harry Kane (Media 90)",
            rating = 90,
            position = "DC",
            club = "Bayern München",
            league = "Bundesliga",
            nation = "Inglaterra",
            cardType = "Oro Único (Fodder)",
            futbinPriceConsole = 34500,
            futbinPricePc = 36000,
            futggPriceConsole = 38500,
            futggPricePc = 40500,
            lowestBinConsole = 34000,
            lowestBinPc = 35500,
            dailyMin = 33000,
            dailyMax = 41000,
            priceHistory24h = listOf(35000, 34200, 33800, 34000, 36000, 38000, 34500),
            priceChange24h = -3.2f,
            chemStyleShadowPriceConsole = 35000,
            chemStyleHunterPriceConsole = 36000,
            isFodder = true,
            promoUpcoming = true,
            leakNotes = "SBC Icono garantizado filtrado para este viernes. Precio histórico de medias 90 suele rozar 48k."
        ),
        PlayerItem(
            id = "fodder_88_bernardo",
            name = "Bernardo Silva (Media 88)",
            rating = 88,
            position = "MC / ED",
            club = "Manchester City",
            league = "Premier League",
            nation = "Portugal",
            cardType = "Oro Único (Fodder)",
            futbinPriceConsole = 16250,
            futbinPricePc = 17500,
            futggPriceConsole = 18400,
            futggPricePc = 19500,
            lowestBinConsole = 16000,
            lowestBinPc = 17200,
            dailyMin = 15800,
            dailyMax = 19500,
            priceHistory24h = listOf(17000, 16800, 16200, 16000, 17200, 18000, 16250),
            priceChange24h = -1.5f,
            chemStyleShadowPriceConsole = 16500,
            chemStyleHunterPriceConsole = 17000,
            isFodder = true,
            promoUpcoming = true,
            leakNotes = "Medias 88 en suelo absoluto. Rebote de +20% proyectado al salir el nuevo SBC de Jugador del Mes (POTM)."
        ),
        PlayerItem(
            id = "fodder_87_bruno",
            name = "Bruno Fernandes (Media 87)",
            rating = 87,
            position = "MCO / MC",
            club = "Manchester United",
            league = "Premier League",
            nation = "Portugal",
            cardType = "Oro Único (Fodder)",
            futbinPriceConsole = 10500,
            futbinPricePc = 11500,
            futggPriceConsole = 12200,
            futggPricePc = 13000,
            lowestBinConsole = 10250,
            lowestBinPc = 11200,
            dailyMin = 10000,
            dailyMax = 13000,
            priceHistory24h = listOf(11500, 11000, 10600, 10400, 11200, 12000, 10500),
            priceChange24h = -2.8f,
            chemStyleShadowPriceConsole = 10800,
            chemStyleHunterPriceConsole = 11200,
            isFodder = true,
            promoUpcoming = true,
            leakNotes = "Medias 87 suben inmediatamente a 14.5k cuando piden plantillas 86 con 2 jugadores de media 87+."
        ),
        PlayerItem(
            id = "fodder_86_modric",
            name = "Luka Modrić (Media 86)",
            rating = 86,
            position = "MC",
            club = "Real Madrid",
            league = "LaLiga EA Sports",
            nation = "Croacia",
            cardType = "Oro Único (Fodder)",
            futbinPriceConsole = 6800,
            futbinPricePc = 7400,
            futggPriceConsole = 7800,
            futggPricePc = 8300,
            lowestBinConsole = 6700,
            lowestBinPc = 7300,
            dailyMin = 6500,
            dailyMax = 8200,
            priceHistory24h = listOf(7200, 7000, 6800, 6700, 7100, 7600, 6800),
            priceChange24h = 1.1f,
            chemStyleShadowPriceConsole = 7200,
            chemStyleHunterPriceConsole = 7000,
            isFodder = true,
            promoUpcoming = false,
            leakNotes = "Inversión ultra segura de bajo presupuesto. Compra masiva a 6.7k y venta garantizada a 8.5k en mejoras."
        )
    )

    private val _players = MutableStateFlow<List<PlayerItem>>(initialPlayers)
    val players: StateFlow<List<PlayerItem>> = _players.asStateFlow()

    private val _currentPlatform = MutableStateFlow(Platform.CONSOLE)
    val currentPlatform: StateFlow<Platform> = _currentPlatform.asStateFlow()

    private val _newsFeed = MutableStateFlow<List<Fc27NewsItem>>(
        listOf(
            Fc27NewsItem(
                id = "leak_trailblazers_t2",
                title = "FILTRACIÓN: Trailblazers Equipo 2 confirmado para este viernes",
                headline = "Lamine Yamal, Jude Bellingham y Saliba recibirán nuevas versiones con PlayStyle+ exclusivos.",
                category = NewsCategory.PROMO_LEAK,
                dateDisplay = "Hoy, 16:30",
                source = "Futbin Leaks & FUT.GG Insights",
                content = "Se han desencriptado los paquetes de la promo Trailblazers Equipo 2 en los servidores de prueba de EA FC 27. Jugadores clave como Bellingham (Pase Incisivo+), Saliba (Anticipación+) y Yamal tendrán cartas especiales. Esto provocará un pánico inicial en sus cartas de oro base, seguido de un efecto 'Out of Packs' durante 7 días.",
                affectedPlayerNames = listOf("Lamine Yamal", "Jude Bellingham", "William Saliba", "Erling Haaland"),
                targetRatings = listOf(87, 88, 90, 91),
                recommendedSignal = TradingSignal.STRONG_BUY,
                tradingTip = "Comprar cartas oro en el punto de pánico máximo (viernes 18:30 a 19:30). Mantener y vender domingo por la tarde tras agotarse la oferta.",
                buyTargetWindow = "Viernes 18:30 - 20:00 CEST",
                sellTargetWindow = "Domingo 17:00 - Lunes 12:00"
            ),
            Fc27NewsItem(
                id = "sbc_hero_flashback_leak",
                title = "ALERTA SBC: Mejora de Héroe Base y SBC Icono garantizado",
                headline = "Requisitos filtrados exigen plantillas de media 86, 87 y 88 con jugadores TOTW.",
                category = NewsCategory.SBC_EVENT,
                dateDisplay = "Hoy, 14:15",
                source = "EA Sports Database Scraper",
                content = "El código fuente de EA Sports Ultimate Team actualizó los desafíos de creación de plantillas (SBC) para la noche de entrega de contenido. Se requerirán 1 plantilla de 86, 1 plantilla de 87 y 1 plantilla de 88. El precio de las medias 87 y 88 actualmente está en mínimos semanales.",
                affectedPlayerNames = listOf("Harry Kane (Media 90)", "Bernardo Silva (Media 88)", "Bruno Fernandes (Media 87)", "Luka Modrić (Media 86)"),
                targetRatings = listOf(86, 87, 88, 89, 90),
                recommendedSignal = TradingSignal.STRONG_BUY,
                tradingTip = "Llenar la lista de transferibles con cartas de medias 87 a 10.5k-11k y medias 88 a 16k-16.5k. Subirán entre un 18% y un 28% tras el lanzamiento del SBC.",
                buyTargetWindow = "Ahora mismo antes de las 18:00 UTC",
                sellTargetWindow = "A los 45 minutos del lanzamiento del SBC"
            ),
            Fc27NewsItem(
                id = "totw_prediction_week_4",
                title = "PREDICCIÓN TOTW 4: Haaland y Vinícius apuntan al nuevo Equipo de la Semana",
                headline = "Las cartas oro de Haaland saldrán de sobres por 7 días seguidos.",
                category = NewsCategory.TOTW_PREDICTION,
                dateDisplay = "Ayer, 21:00",
                source = "FUT.GG Market Radar",
                content = "Erling Haaland selló un hat-trick magistral en Premier League y Vinícius Jr dio 2 asistencias y 1 gol. Cuando un jugador de alta demanda entra al TOTW, su versión oro deja de salir en sobres, reduciendo el suministro un 100% mientras la demanda en FUT Champions se mantiene alta.",
                affectedPlayerNames = listOf("Erling Haaland", "Vinícius Jr."),
                targetRatings = listOf(90, 91),
                recommendedSignal = TradingSignal.BUY,
                tradingTip = "Comprar la carta oro de Haaland en el reset de recompensas del jueves por la mañana. Vender sábado por la mañana con margen neto del 12-15%.",
                buyTargetWindow = "Jueves 09:30 - 11:00 (Tras recompensas Rivals)",
                sellTargetWindow = "Sábado 12:00 - 15:00"
            ),
            Fc27NewsItem(
                id = "market_pre_black_friday_alert",
                title = "ALERTA DE MERCADO: Comportamiento previo al Black Friday en FC 27",
                headline = "Historial de pánico masivo y cómo aprovechar los rebotes en jugadores meta.",
                category = NewsCategory.CRASH_ALERT,
                dateDisplay = "2 Oct, 18:00",
                source = "Futbin Economic Analysts",
                content = "Muchos usuarios inexpertos venden sus equipos semanas antes esperando una bajada. Esto provoca que el mínimo de mercado se adelante a finales de octubre y principios de noviembre. Los jugadores meta como Mbappé y Van Dijk sufren micro-caídas que rebotan con fuerza en pocas horas.",
                affectedPlayerNames = listOf("Kylian Mbappé", "Virgil van Dijk", "Theo Hernández"),
                targetRatings = listOf(87, 89, 91),
                recommendedSignal = TradingSignal.HOLD,
                tradingTip = "No entrar en pánico. Colocar ofertas de puja un 10-15% por debajo de BIN en horas nocturnas (02:00 - 05:00 CEST) cuando hay menos competencia.",
                buyTargetWindow = "Madrugadas de lunes a miércoles",
                sellTargetWindow = "Jueves / Viernes pre-Champions"
            )
        )
    )
    val newsFeed: StateFlow<List<Fc27NewsItem>> = _newsFeed.asStateFlow()

    fun setPlatform(platform: Platform) {
        _currentPlatform.value = platform
    }

    /**
     * Simulates real-time price updates from Futbin and FUT.GG feeds
     * with live spread variations, tick volatility and updated calculations.
     */
    fun refreshLiveQuotes() {
        val updated = _players.value.map { player ->
            // Subtle realistic price fluctuation (-1.5% to +1.8%)
            val deltaPct = (Random.nextFloat() * 3.3f - 1.5f) / 100f
            val newFutbinConsole = (player.futbinPriceConsole * (1f + deltaPct)).toInt().coerceAtLeast(1000)
            val newFutggConsole = (player.futggPriceConsole * (1f + (deltaPct + (Random.nextFloat() * 0.01f - 0.005f)))).toInt().coerceAtLeast(1000)
            val newLowest = (newFutbinConsole * 0.985f).toInt()

            player.copy(
                futbinPriceConsole = newFutbinConsole,
                futggPriceConsole = newFutggConsole,
                lowestBinConsole = newLowest,
                priceChange24h = player.priceChange24h + (deltaPct * 100f).coerceIn(-1.5f, 1.5f)
            )
        }
        _players.value = updated
    }
}
