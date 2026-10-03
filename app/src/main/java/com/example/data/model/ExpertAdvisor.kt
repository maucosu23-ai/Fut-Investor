package com.example.data.model

import com.example.util.EaTaxCalculator

data class ExpertProfile(
    val id: String,
    val name: String,
    val handle: String,
    val specialty: String,
    val accuracyPercent: Int,
    val tradingStyle: String,
    val riskLevel: RiskLevel,
    val avatarBadgeHex: Long
)

data class ExpertRecommendation(
    val id: String,
    val expert: ExpertProfile,
    val player: PlayerItem,
    val signal: TradingSignal,
    val targetBuyPrice: Int,
    val targetSellPrice: Int,
    val timeHorizon: String,
    val thesis: String,
    val tags: List<String>,
    val upvotes: Int,
    val timestampDisplay: String
) {
    val eaTax: Int get() = EaTaxCalculator.calculateTax(targetSellPrice)
    val netProfit: Int get() = EaTaxCalculator.calculateNetProfit(targetBuyPrice, targetSellPrice)
    val roiPercent: Float get() = EaTaxCalculator.calculateRoi(targetBuyPrice, targetSellPrice)
    val isProfitable: Boolean get() = netProfit > 0
}

data class ExpertConsensus(
    val player: PlayerItem,
    val overallSignal: TradingSignal,
    val buyVotes: Int,
    val holdVotes: Int,
    val sellVotes: Int,
    val consensusPercent: Int,
    val avgBuyTarget: Int,
    val avgSellTarget: Int,
    val avgNetProfit: Int,
    val expertTheses: List<Pair<ExpertProfile, String>>
)
