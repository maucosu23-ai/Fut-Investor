package com.example.util

import kotlin.math.ceil

/**
 * Utility helper that calculates the official EA Sports FC 5% transfer market sales tax,
 * proceeds after tax, net profit, return on investment (ROI %), and breakeven prices.
 */
object EaTaxCalculator {

    const val EA_TAX_RATE = 0.05
    const val NET_PROCEEDS_MULTIPLIER = 0.95

    /**
     * Calculates the exact 5% EA sales tax deducted on any transfer market sale.
     * Example: 100,000 coins sale -> 5,000 coins tax.
     */
    fun calculateTax(sellPrice: Int): Int {
        if (sellPrice <= 0) return 0
        return (sellPrice * EA_TAX_RATE).toInt()
    }

    /**
     * Calculates the gross proceeds credited to the user's club after deducting the 5% EA tax.
     * Example: 100,000 coins sale -> 95,000 coins received.
     */
    fun calculateProceedsAfterTax(sellPrice: Int): Int {
        if (sellPrice <= 0) return 0
        return sellPrice - calculateTax(sellPrice)
    }

    /**
     * Calculates net profit or loss after deducting the 5% EA tax from the sell price
     * and subtracting the initial purchase cost.
     * Formula: (sellPrice * 0.95) - buyPrice
     */
    fun calculateNetProfit(buyPrice: Int, sellPrice: Int): Int {
        val proceeds = calculateProceedsAfterTax(sellPrice)
        return proceeds - buyPrice
    }

    /**
     * Calculates the minimum selling price needed to recover the initial purchase cost
     * without losing coins after EA's 5% tax cut.
     * Formula: ceil(buyPrice / 0.95)
     */
    fun calculateBreakevenPrice(buyPrice: Int): Int {
        if (buyPrice <= 0) return 0
        return ceil(buyPrice / NET_PROCEEDS_MULTIPLIER).toInt()
    }

    /**
     * Calculates Return on Investment (ROI %) based on net profit over initial cost.
     */
    fun calculateRoi(buyPrice: Int, sellPrice: Int): Float {
        if (buyPrice <= 0) return 0f
        val net = calculateNetProfit(buyPrice, sellPrice)
        return (net.toFloat() / buyPrice) * 100f
    }

    /**
     * Complete comprehensive tax breakdown result.
     */
    data class TaxCalculationResult(
        val buyPrice: Int,
        val sellPrice: Int,
        val taxAmount: Int,
        val proceedsAfterTax: Int,
        val netProfit: Int,
        val roiPercent: Float,
        val breakevenPrice: Int,
        val isProfitable: Boolean
    )

    fun calculate(buyPrice: Int, sellPrice: Int): TaxCalculationResult {
        val tax = calculateTax(sellPrice)
        val proceeds = calculateProceedsAfterTax(sellPrice)
        val net = proceeds - buyPrice
        val roi = if (buyPrice > 0) (net.toFloat() / buyPrice) * 100f else 0f
        val breakeven = calculateBreakevenPrice(buyPrice)

        return TaxCalculationResult(
            buyPrice = buyPrice,
            sellPrice = sellPrice,
            taxAmount = tax,
            proceedsAfterTax = proceeds,
            netProfit = net,
            roiPercent = roi,
            breakevenPrice = breakeven,
            isProfitable = net > 0
        )
    }
}
