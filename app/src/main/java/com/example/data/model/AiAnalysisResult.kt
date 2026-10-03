package com.example.data.model

data class AiAnalysisResult(
    val queryOrPlayer: String,
    val signal: TradingSignal,
    val targetBuyPrice: Int,
    val targetSellPrice: Int,
    val expectedNetProfit: Int,
    val expectedRoi: Float,
    val riskLevel: RiskLevel,
    val timeHorizon: String,
    val analysisReasoning: String,
    val promoImpactFactor: String,
    val eaTaxBreakdown: String,
    val actionChecklist: List<String>
)
