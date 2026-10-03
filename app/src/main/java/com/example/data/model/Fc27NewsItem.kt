package com.example.data.model

data class Fc27NewsItem(
    val id: String,
    val title: String,
    val headline: String,
    val category: NewsCategory,
    val dateDisplay: String,
    val source: String,
    val content: String,
    val affectedPlayerNames: List<String>,
    val targetRatings: List<Int>,
    val recommendedSignal: TradingSignal,
    val tradingTip: String,
    val buyTargetWindow: String,
    val sellTargetWindow: String
)
