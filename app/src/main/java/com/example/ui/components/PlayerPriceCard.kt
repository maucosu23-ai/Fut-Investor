package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Platform
import com.example.data.model.PlayerItem
import com.example.ui.theme.FutBorder
import com.example.ui.theme.FutCyanAccent
import com.example.ui.theme.FutGold
import com.example.ui.theme.FutGoldBright
import com.example.ui.theme.FutGreenDark
import com.example.ui.theme.FutGreenProfit
import com.example.ui.theme.FutRedTax
import com.example.ui.theme.FutSurface
import com.example.ui.theme.FutSurfaceElevated
import com.example.ui.theme.FutSurfaceVariant
import com.example.ui.theme.FutTextMuted
import com.example.ui.theme.FutTextPrimary
import com.example.ui.theme.FutTextSecondary
import com.example.ui.theme.FutbinBrand
import com.example.ui.theme.FutggBrand
import java.text.NumberFormat
import java.util.Locale

fun formatCoins(coins: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY) // Formats as 1.234.567
    return "${formatter.format(coins)} 🪙"
}

@Composable
fun PlayerPriceCard(
    player: PlayerItem,
    platform: Platform,
    isInWatchlist: Boolean,
    onWatchlistToggle: () -> Unit,
    onAiConsultClick: () -> Unit,
    onCardClick: () -> Unit,
    onCalcClick: () -> Unit
) {
    val futbinPrice = player.getFutbinPrice(platform)
    val futggPrice = player.getFutggPrice(platform)
    val lowestBin = player.getLowestBin(platform)
    val spread = player.getMarketSpread(platform)

    // Arbitrage calculations
    val cheaper = minOf(futbinPrice, futggPrice)
    val higher = maxOf(futbinPrice, futggPrice)
    val eaTax = (higher * 0.05).toInt()
    val netProfit = (higher * 0.95).toInt() - cheaper
    val roi = if (cheaper > 0) (netProfit.toFloat() / cheaper) * 100f else 0f
    val isArbitrageViable = netProfit > 500

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("player_card_${player.id}")
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isArbitrageViable) FutGreenProfit.copy(alpha = 0.5f) else FutBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Player Card Badge, Name, Rating, Position, Watchlist Star
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Rating & Position FUT Shield
                    Box(
                        modifier = Modifier
                            .size(width = 46.dp, height = 48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(FutGoldBright, FutGold, Color(0xFF6B4E00))
                                )
                            )
                            .border(1.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${player.rating}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                            Text(
                                text = player.position,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = player.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutTextPrimary
                            )
                            if (player.promoUpcoming) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(FutCyanAccent.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "LEAK FC27",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FutCyanAccent
                                    )
                                }
                            }
                        }

                        Text(
                            text = "${player.club} • ${player.league}",
                            fontSize = 12.sp,
                            color = FutTextSecondary
                        )

                        Text(
                            text = player.cardType,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = FutGold
                        )
                    }
                }

                // Sparkline and Watchlist Button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PriceSparkline(
                        history = player.priceHistory24h,
                        isPositive = player.priceChange24h >= 0,
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    IconButton(
                        onClick = onWatchlistToggle,
                        modifier = Modifier.testTag("watchlist_toggle_${player.id}")
                    ) {
                        Icon(
                            imageVector = if (isInWatchlist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Guardar en Radar",
                            tint = if (isInWatchlist) FutGold else FutTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comparative Price Columns: Futbin vs FUT.GG
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FutSurface)
                    .border(1.dp, FutBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // FUTBIN Box
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(FutbinBrand)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "FUTBIN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutbinBrand
                        )
                    }
                    Text(
                        text = formatCoins(futbinPrice),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = FutTextPrimary,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (player.priceChange24h >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = if (player.priceChange24h >= 0) FutGreenProfit else FutRedTax,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${if (player.priceChange24h >= 0) "+" else ""}${String.format(Locale.US, "%.1f", player.priceChange24h)}%",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (player.priceChange24h >= 0) FutGreenProfit else FutRedTax
                        )
                    }
                }

                // Vertical Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(36.dp)
                        .background(FutBorder)
                )

                // FUT.GG Box
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(FutggBrand)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "FUT.GG",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutggBrand
                        )
                    }
                    Text(
                        text = formatCoins(futggPrice),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = FutTextPrimary,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Text(
                        text = "Lowest BIN: ${formatCoins(lowestBin)}",
                        fontSize = 10.sp,
                        color = FutTextMuted
                    )
                }
            }

            // Arbitrage Spread & EA Tax Alert Box
            if (spread > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isArbitrageViable) FutGreenDark else FutSurfaceElevated)
                        .border(
                            1.dp,
                            if (isArbitrageViable) FutGreenProfit.copy(alpha = 0.5f) else FutBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Desfase Futbin vs FUT.GG: ",
                                fontSize = 11.sp,
                                color = FutTextSecondary
                            )
                            Text(
                                text = formatCoins(spread),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutGold
                            )
                        }
                        Text(
                            text = "Tasa EA 5%: -${formatCoins(eaTax)}",
                            fontSize = 10.sp,
                            color = FutRedTax
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (isArbitrageViable) "Neto: +${formatCoins(netProfit)}" else "Sin margen rentable",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isArbitrageViable) FutGreenProfit else FutTextMuted
                        )
                        if (isArbitrageViable) {
                            Text(
                                text = "ROI +${String.format(Locale.US, "%.1f", roi)}%",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutGreenProfit
                            )
                        }
                    }
                }
            }

            // Action Buttons: AI Recommendation & Tax Calculator
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onAiConsultClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("ai_consult_${player.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FutGold,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Consejo IA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onCalcClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("calc_tax_${player.id}"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = FutTextPrimary
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FutBorder),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = FutCyanAccent,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Calcular 5%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
