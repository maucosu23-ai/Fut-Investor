package com.example

import com.example.data.model.Platform
import com.example.data.model.PlayerItem
import com.example.data.repository.ArbitrageEngine
import com.example.util.EaTaxCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testEa5PercentTaxCalculationHelper() {
        val sellPrice = 100000
        val tax = EaTaxCalculator.calculateTax(sellPrice)
        assertEquals(5000, tax) // Exactly 5%

        val proceeds = EaTaxCalculator.calculateProceedsAfterTax(sellPrice)
        assertEquals(95000, proceeds) // 95% proceeds

        val buyPrice = 80000
        val netProfit = EaTaxCalculator.calculateNetProfit(buyPrice, sellPrice)
        assertEquals(15000, netProfit) // 95,000 - 80,000 = 15,000

        val roi = EaTaxCalculator.calculateRoi(buyPrice, sellPrice)
        assertEquals(18.75f, roi, 0.01f) // 15,000 / 80,000 * 100% = 18.75%
    }

    @Test
    fun testEaTaxCalculatorBreakeven() {
        val buyPrice = 50000
        val breakeven = EaTaxCalculator.calculateBreakevenPrice(buyPrice)
        // Selling at breakeven after 5% tax must be >= buyPrice
        val proceedsAtBreakeven = EaTaxCalculator.calculateProceedsAfterTax(breakeven)
        assertTrue(proceedsAtBreakeven >= buyPrice)
    }

    @Test
    fun testEaTaxCalculatorComprehensiveResult() {
        val resultProfitable = EaTaxCalculator.calculate(buyPrice = 100000, sellPrice = 120000)
        assertEquals(6000, resultProfitable.taxAmount) // 5% of 120k
        assertEquals(114000, resultProfitable.proceedsAfterTax)
        assertEquals(14000, resultProfitable.netProfit)
        assertTrue(resultProfitable.isProfitable)
        assertEquals(11.66f, resultProfitable.roiPercent, 0.02f)

        // Loss scenario
        val resultLoss = EaTaxCalculator.calculate(buyPrice = 100000, sellPrice = 102000)
        assertEquals(5100, resultLoss.taxAmount)
        assertEquals(96900, resultLoss.proceedsAfterTax)
        assertEquals(-3100, resultLoss.netProfit)
        assertFalse(resultLoss.isProfitable)
    }

    @Test
    fun testArbitrageEngineDetection() {
        val testPlayer = PlayerItem(
            id = "test_player",
            name = "Test Striker",
            rating = 88,
            position = "ST",
            club = "Test FC",
            league = "Test League",
            nation = "Spain",
            cardType = "Oro Único",
            futbinPriceConsole = 50000,
            futbinPricePc = 55000,
            futggPriceConsole = 62000,
            futggPricePc = 68000,
            lowestBinConsole = 49000,
            lowestBinPc = 54000,
            dailyMin = 48000,
            dailyMax = 63000,
            priceHistory24h = listOf(49000, 50000, 52000, 62000),
            priceChange24h = 5.0f,
            chemStyleShadowPriceConsole = 58000,
            chemStyleHunterPriceConsole = 60000
        )

        val opportunities = ArbitrageEngine.scanOpportunities(
            players = listOf(testPlayer),
            platform = Platform.CONSOLE
        )

        assertTrue(opportunities.isNotEmpty())
        val cross = opportunities.firstOrNull { it.player.id == "test_player" }
        assertTrue(cross != null)
        assertTrue(cross!!.netProfit > 0)
    }

    @Test
    fun testExpertRepositoryRecommendationsAndConsensus() {
        val testPlayer = PlayerItem(
            id = "yamal_88_rttk",
            name = "Lamine Yamal",
            rating = 88,
            position = "ED",
            club = "FC Barcelona",
            league = "LaLiga",
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
            priceHistory24h = listOf(595000, 620000),
            priceChange24h = 8.4f,
            chemStyleShadowPriceConsole = 635000,
            chemStyleHunterPriceConsole = 660000,
            promoUpcoming = true
        )

        val recs = com.example.data.repository.ExpertRepository.getRecommendations(listOf(testPlayer))
        assertTrue(recs.isNotEmpty())

        val consensus = com.example.data.repository.ExpertRepository.buildConsensus(testPlayer)
        assertTrue(consensus.consensusPercent >= 50)
        assertTrue(consensus.avgNetProfit > 0)
        assertTrue(consensus.expertTheses.isNotEmpty())
    }
}
