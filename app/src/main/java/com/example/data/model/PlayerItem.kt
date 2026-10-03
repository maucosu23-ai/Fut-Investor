package com.example.data.model

data class PlayerItem(
    val id: String,
    val name: String,
    val rating: Int,
    val position: String,
    val club: String,
    val league: String,
    val nation: String,
    val cardType: String, // "Oro Único", "Trailblazers", "RTTK", "TOTW", "Icono", "Héroe", "POTM"
    val futbinPriceConsole: Int,
    val futbinPricePc: Int,
    val futggPriceConsole: Int,
    val futggPricePc: Int,
    val lowestBinConsole: Int,
    val lowestBinPc: Int,
    val dailyMin: Int,
    val dailyMax: Int,
    val priceHistory24h: List<Int>,
    val priceChange24h: Float, // percentage e.g. +4.5%
    val chemStyleShadowPriceConsole: Int,
    val chemStyleHunterPriceConsole: Int,
    val isFodder: Boolean = false,
    val promoUpcoming: Boolean = false,
    val leakNotes: String = ""
) {
    fun getFutbinPrice(platform: Platform): Int =
        if (platform == Platform.CONSOLE) futbinPriceConsole else futbinPricePc

    fun getFutggPrice(platform: Platform): Int =
        if (platform == Platform.CONSOLE) futggPriceConsole else futggPricePc

    fun getLowestBin(platform: Platform): Int =
        if (platform == Platform.CONSOLE) lowestBinConsole else lowestBinPc

    fun getMarketSpread(platform: Platform): Int {
        val fbin = getFutbinPrice(platform)
        val fgg = getFutggPrice(platform)
        return kotlin.math.abs(fbin - fgg)
    }

    /**
     * EA FC applies a strict 5% tax on every transfer market sale.
     */
    fun calculateEaTax(sellPrice: Int): Int =
        com.example.util.EaTaxCalculator.calculateTax(sellPrice)

    fun calculateBreakeven(buyPrice: Int): Int =
        com.example.util.EaTaxCalculator.calculateBreakevenPrice(buyPrice)

    fun calculateNetProfit(buyPrice: Int, sellPrice: Int): Int =
        com.example.util.EaTaxCalculator.calculateNetProfit(buyPrice, sellPrice)

    fun calculateRoi(buyPrice: Int, sellPrice: Int): Float =
        com.example.util.EaTaxCalculator.calculateRoi(buyPrice, sellPrice)
}
