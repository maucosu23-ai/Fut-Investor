package com.example.data.repository

import com.example.data.model.ExpertConsensus
import com.example.data.model.ExpertProfile
import com.example.data.model.ExpertRecommendation
import com.example.data.model.PlayerItem
import com.example.data.model.RiskLevel
import com.example.data.model.TradingSignal
import com.example.util.EaTaxCalculator

object ExpertRepository {

    val experts = listOf(
        ExpertProfile(
            id = "exp_futbin_quant",
            name = "Futbin Quant Desk",
            handle = "@FutbinData",
            specialty = "Desfase Futbin vs FUT.GG & Spreads",
            accuracyPercent = 95,
            tradingStyle = "Arbitraje Algorítmico",
            riskLevel = RiskLevel.LOW,
            avatarBadgeHex = 0xFFFF7A00
        ),
        ExpertProfile(
            id = "exp_futgg_meta",
            name = "FUT.GG Pro Analytics",
            handle = "@FutggInsights",
            specialty = "Meta Ratings & Demanda Weekend League",
            accuracyPercent = 93,
            tradingStyle = "Fluctuación Semanal",
            riskLevel = RiskLevel.MEDIUM,
            avatarBadgeHex = 0xFF8B5CF6
        ),
        ExpertProfile(
            id = "exp_leaks_insider",
            name = "FUT Leaks VIP Desk",
            handle = "@SheriffTrading",
            specialty = "Promos FC 27, TOTW & Out of Packs",
            accuracyPercent = 97,
            tradingStyle = "Especulación de Filtraciones",
            riskLevel = RiskLevel.MEDIUM,
            avatarBadgeHex = 0xFF00E5FF
        ),
        ExpertProfile(
            id = "exp_fodder_economist",
            name = "Wall Street FC",
            handle = "@FUTEconomist",
            specialty = "Medias 86-90 & Retorno SBCs",
            accuracyPercent = 94,
            tradingStyle = "Inversión Fodder a Gran Escala",
            riskLevel = RiskLevel.LOW,
            avatarBadgeHex = 0xFF00E676
        ),
        ExpertProfile(
            id = "exp_sniping_king",
            name = "Sniping & Chem Master",
            handle = "@SnipeKingFC",
            specialty = "Filtros 59m & Química Sombra/Cazador",
            accuracyPercent = 91,
            tradingStyle = "Flip Rápido Intradiario",
            riskLevel = RiskLevel.LOW,
            avatarBadgeHex = 0xFFFFD54F
        )
    )

    fun getRecommendations(players: List<PlayerItem>): List<ExpertRecommendation> {
        val yamal = players.find { it.id == "yamal_88_rttk" } ?: players.first()
        val mbappe = players.find { it.id == "mbappe_91_gold" } ?: players.first()
        val haaland = players.find { it.id == "haaland_91_gold" } ?: players.first()
        val saliba = players.find { it.id == "saliba_87_totw" } ?: players.first()
        val kane = players.find { it.id == "fodder_88_kane" } ?: players.last()
        val bernardo = players.find { it.id == "fodder_88_bernardo" } ?: players.last()
        val bruno = players.find { it.id == "fodder_87_bruno" } ?: players.last()
        val theo = players.find { it.id == "theo_87_gold" } ?: players.first()
        val vandijk = players.find { it.id == "vandijk_89_gold" } ?: players.first()

        return listOf(
            ExpertRecommendation(
                id = "rec_1",
                expert = experts[2], // Leaks VIP
                player = yamal,
                signal = TradingSignal.STRONG_BUY,
                targetBuyPrice = 615000,
                targetSellPrice = 695000,
                timeHorizon = "3 Días (Pre-Upgrade)",
                thesis = "Filtración confirmada: Próximo partido de Champions casi asegura subida a 89. El pánico actual deja una ventana dorada de compra.",
                tags = listOf("Promo Leak", "RTTK Champions", "Alto Retorno"),
                upvotes = 342,
                timestampDisplay = "Hace 15m"
            ),
            ExpertRecommendation(
                id = "rec_2",
                expert = experts[3], // Wall Street FC
                player = bernardo,
                signal = TradingSignal.STRONG_BUY,
                targetBuyPrice = 16250,
                targetSellPrice = 20500,
                timeHorizon = "24-48h (SBC Viernes)",
                thesis = "Las medias 88 están en el suelo técnico de mercado (16.2k). Al lanzarse el SBC de Icono garantizado rebotarán un +25% mínimo.",
                tags = listOf("Medias Fodder", "Presupuesto Bajo", "Riesgo Mínimo"),
                upvotes = 289,
                timestampDisplay = "Hace 35m"
            ),
            ExpertRecommendation(
                id = "rec_3",
                expert = experts[0], // Futbin Quant
                player = saliba,
                signal = TradingSignal.BUY,
                targetBuyPrice = 282000,
                targetSellPrice = 320000,
                timeHorizon = "Flip 2 Horas",
                thesis = "Desfase claro: En Futbin se liquida a 285k pero en FUT.GG la absorción está en 310k. Margen limpio garantizado con filtro de compra rápida.",
                tags = listOf("Cross-Market", "Premier League", "Meta"),
                upvotes = 215,
                timestampDisplay = "Hace 1h"
            ),
            ExpertRecommendation(
                id = "rec_4",
                expert = experts[4], // Sniping King
                player = vandijk,
                signal = TradingSignal.BUY,
                targetBuyPrice = 194000,
                targetSellPrice = 224000,
                timeHorizon = "Intradiario",
                thesis = "Comprar con Estilo Sombra a precio básico de mercado (194k) y relistar a 224k. Deja más de 18.000 monedas limpias por carta.",
                tags = listOf("Estilo Química", "Sombra", "Meta CB"),
                upvotes = 198,
                timestampDisplay = "Hace 2h"
            ),
            ExpertRecommendation(
                id = "rec_5",
                expert = experts[1], // FUT.GG Meta
                player = haaland,
                signal = TradingSignal.BUY,
                targetBuyPrice = 174000,
                targetSellPrice = 202000,
                timeHorizon = "Fin de Semana",
                thesis = "Si entra al TOTW tras su hat-trick, saldrá de sobres durante 7 días. La falta de suministro provocará una subida forzada del oro base.",
                tags = listOf("TOTW", "Out of Packs", "Striker Meta"),
                upvotes = 267,
                timestampDisplay = "Hace 2h"
            ),
            ExpertRecommendation(
                id = "rec_6",
                expert = experts[3], // Wall Street FC
                player = kane,
                signal = TradingSignal.STRONG_BUY,
                targetBuyPrice = 34500,
                targetSellPrice = 43000,
                timeHorizon = "Pre-SBC Icono",
                thesis = "Medias 90 a 34k es una anomalía histórica. Los requisitos filtrados piden plantillas 88 donde Kane es la pieza más barata por rating.",
                tags = listOf("Medias Fodder", "Media 90", "Inversión Segura"),
                upvotes = 310,
                timestampDisplay = "Hace 3h"
            ),
            ExpertRecommendation(
                id = "rec_7",
                expert = experts[4], // Sniping King
                player = theo,
                signal = TradingSignal.BUY,
                targetBuyPrice = 126000,
                targetSellPrice = 145000,
                timeHorizon = "Rápido (1h)",
                thesis = "Sniping agresivo en el minuto 59 para cazar cartas listadas a 125k. Venta instantánea a 145k en las horas previas a FUT Champions.",
                tags = listOf("Sniping", "Serie A", "Pace LB"),
                upvotes = 184,
                timestampDisplay = "Hace 4h"
            ),
            ExpertRecommendation(
                id = "rec_8",
                expert = experts[0], // Futbin Quant
                player = bruno,
                signal = TradingSignal.STRONG_BUY,
                targetBuyPrice = 10500,
                targetSellPrice = 13800,
                timeHorizon = "24 Horas",
                thesis = "Medias 87 a 10.5k monedas. Retorno neto proyectado superior al 24% al activarse mejoras de plantilla.",
                tags = listOf("Medias Fodder", "Media 87", "Presupuesto Bajo"),
                upvotes = 245,
                timestampDisplay = "Hace 5h"
            )
        )
    }

    /**
     * Builds multi-expert consensus for a player to compare different analyst points of view.
     */
    fun buildConsensus(player: PlayerItem): ExpertConsensus {
        val buyVotes = if (player.isFodder || player.promoUpcoming) 4 else 3
        val holdVotes = if (player.rating >= 91) 2 else 1
        val sellVotes = 0

        val baseCheaper = minOf(player.futbinPriceConsole, player.futggPriceConsole)
        val avgBuy = (baseCheaper * 0.97f).toInt()
        val avgSell = (maxOf(player.futbinPriceConsole, player.futggPriceConsole) * 1.12f).toInt()
        val avgNet = EaTaxCalculator.calculateNetProfit(avgBuy, avgSell)

        val theses = listOf(
            Pair(experts[0], "Desfase detectado en order book. Soporte firme en $avgBuy monedas con salida en $avgSell."),
            Pair(experts[2], "Filtraciones apuntan a alta demanda por SBC o química en el próximo ciclo de contenido."),
            Pair(experts[1], "Carta con ratio de uso superior al 28% en FUT Champions. Rebote seguro de fin de semana.")
        )

        return ExpertConsensus(
            player = player,
            overallSignal = if (player.promoUpcoming || player.isFodder) TradingSignal.STRONG_BUY else TradingSignal.BUY,
            buyVotes = buyVotes,
            holdVotes = holdVotes,
            sellVotes = sellVotes,
            consensusPercent = ((buyVotes.toFloat() / (buyVotes + holdVotes + sellVotes)) * 100).toInt(),
            avgBuyTarget = avgBuy,
            avgSellTarget = avgSell,
            avgNetProfit = avgNet,
            expertTheses = theses
        )
    }
}
