package com.example.data.model

data class ArbitrageOpportunity(
    val id: String,
    val type: ArbitrageType,
    val player: PlayerItem,
    val buyTargetPrice: Int,
    val sellTargetPrice: Int,
    val platform: Platform,
    val riskLevel: RiskLevel,
    val confidencePercent: Int,
    val title: String,
    val strategySummary: String,
    val executionSteps: List<String>,
    val timeHorizon: String,
    val isHotSignal: Boolean = false
) {
    val eaTax: Int get() = (sellTargetPrice * 0.05).toInt()
    val netProfit: Int get() = (sellTargetPrice * 0.95).toInt() - buyTargetPrice
    val roiPercent: Float get() = if (buyTargetPrice > 0) (netProfit.toFloat() / buyTargetPrice) * 100f else 0f
}
