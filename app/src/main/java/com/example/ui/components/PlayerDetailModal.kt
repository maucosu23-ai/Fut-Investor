package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.FutGreenDark
import com.example.ui.theme.FutGreenProfit
import com.example.ui.theme.FutRedDark
import com.example.ui.theme.FutRedTax
import com.example.ui.theme.FutSurface
import com.example.ui.theme.FutSurfaceElevated
import com.example.ui.theme.FutSurfaceVariant
import com.example.ui.theme.FutTextMuted
import com.example.ui.theme.FutTextPrimary
import com.example.ui.theme.FutTextSecondary
import com.example.ui.theme.FutbinBrand
import com.example.ui.theme.FutggBrand
import com.example.util.EaTaxCalculator
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetailModal(
    player: PlayerItem?,
    platform: Platform,
    onDismiss: () -> Unit,
    onAiConsultClick: (PlayerItem) -> Unit,
    onAddToPortfolio: (PlayerItem, Int, Int, String) -> Unit
) {
    if (player == null) return
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val futbinPrice = player.getFutbinPrice(platform)
    val futggPrice = player.getFutggPrice(platform)
    val lowestBin = player.getLowestBin(platform)

    // Interactive simulator inputs
    var buyInput by remember(player) { mutableStateOf(futbinPrice.toString()) }
    var sellInput by remember(player) { mutableStateOf((futggPrice * 1.08f).toInt().toString()) }
    var selectedChem by remember { mutableStateOf("Básico") }

    // Live calculations powered by EaTaxCalculator helper
    val buyAmount by remember { derivedStateOf { buyInput.toIntOrNull() ?: futbinPrice } }
    val sellAmount by remember { derivedStateOf { sellInput.toIntOrNull() ?: futggPrice } }
    val taxResult by remember {
        derivedStateOf { EaTaxCalculator.calculate(buyAmount, sellAmount) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FutSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Player Name, Club, Position, Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${player.rating}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = FutGold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = player.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = FutTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(${player.position})",
                            fontSize = 14.sp,
                            color = FutTextSecondary
                        )
                    }
                    Text(
                        text = "${player.club} • ${player.league} • ${player.cardType}",
                        fontSize = 12.sp,
                        color = FutTextSecondary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = FutTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Real-time Comparison Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(FutSurfaceVariant)
                    .border(1.dp, FutBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = "COTIZACIÓN EN TIEMPO REAL (${platform.label})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Futbin", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FutbinBrand)
                            Text(text = formatCoins(futbinPrice), fontSize = 16.sp, fontWeight = FontWeight.Black, color = FutTextPrimary)
                            Text(text = "Lowest BIN: ${formatCoins(lowestBin)}", fontSize = 10.sp, color = FutTextMuted)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "FUT.GG", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FutggBrand)
                            Text(text = formatCoins(futggPrice), fontSize = 16.sp, fontWeight = FontWeight.Black, color = FutTextPrimary)
                            Text(text = "Rango Diario: ${formatCoins(player.dailyMin)} - ${formatCoins(player.dailyMax)}", fontSize = 10.sp, color = FutTextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Desfase Futbin / FUT.GG: ${formatCoins(player.getMarketSpread(platform))}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutCyanAccent
                        )
                        val spreadNet = EaTaxCalculator.calculateNetProfit(
                            minOf(futbinPrice, futggPrice),
                            maxOf(futbinPrice, futggPrice)
                        )
                        Text(
                            text = "Neto tras 5% EA: +${formatCoins(spreadNet)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (spreadNet > 0) FutGreenProfit else FutRedTax
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =========================================================================
            // DEDICATED UTILITY: CALCULADORA DE IMPUESTO EA 5% & BENEFICIO NETO
            // =========================================================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("player_tax_utility_section"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (taxResult.isProfitable) FutGreenProfit.copy(alpha = 0.7f) else FutBorder
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Utility Title
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = FutGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "UTILIDAD DE TASA EA 5% & BENEFICIO NETO",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = FutGold
                            )
                        }

                        // ROI Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (taxResult.isProfitable) FutGreenProfit.copy(alpha = 0.2f)
                                    else FutRedTax.copy(alpha = 0.2f)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "${if (taxResult.roiPercent >= 0) "+" else ""}${String.format(Locale.US, "%.1f", taxResult.roiPercent)}% ROI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (taxResult.isProfitable) FutGreenProfit else FutRedTax
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Purchase Price Section & Quick Selection Chips
                    Text(
                        text = "Precio de Compra:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = buyInput,
                        onValueChange = { buyInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("utility_buy_price_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = FutSurface,
                            unfocusedContainerColor = FutSurface,
                            focusedBorderColor = FutGold,
                            unfocusedBorderColor = FutBorder,
                            focusedTextColor = FutTextPrimary,
                            unfocusedTextColor = FutTextPrimary
                        )
                    )

                    // Quick Chips to set buy price
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PresetPriceChip(label = "Futbin: ${formatCoins(futbinPrice)}") {
                            buyInput = futbinPrice.toString()
                        }
                        PresetPriceChip(label = "Lowest BIN: ${formatCoins(lowestBin)}") {
                            buyInput = lowestBin.toString()
                        }
                        PresetPriceChip(label = "FUT.GG: ${formatCoins(futggPrice)}") {
                            buyInput = futggPrice.toString()
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Estimated Sell Price Section & Quick % Bump Chips
                    Text(
                        text = "Precio Estimado de Venta:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutTextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = sellInput,
                        onValueChange = { sellInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("utility_sell_price_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = FutSurface,
                            unfocusedContainerColor = FutSurface,
                            focusedBorderColor = FutCyanAccent,
                            unfocusedBorderColor = FutBorder,
                            focusedTextColor = FutTextPrimary,
                            unfocusedTextColor = FutTextPrimary
                        )
                    )

                    // Quick buttons for target sell price (+5%, +10%, +20%, Breakeven)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        PresetPriceChip(label = "+5% Flip") {
                            sellInput = (buyAmount * 1.05f).toInt().toString()
                        }
                        PresetPriceChip(label = "+10% Diario") {
                            sellInput = (buyAmount * 1.10f).toInt().toString()
                        }
                        PresetPriceChip(label = "+20% Pre-SBC") {
                            sellInput = (buyAmount * 1.20f).toInt().toString()
                        }
                        PresetPriceChip(label = "FUT.GG: ${formatCoins(futggPrice)}") {
                            sellInput = futggPrice.toString()
                        }
                        PresetPriceChip(label = "Breakeven (${formatCoins(taxResult.breakevenPrice)})") {
                            sellInput = taxResult.breakevenPrice.toString()
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dynamic Live Net Profit Result Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (taxResult.isProfitable) FutGreenDark else FutRedDark)
                            .border(
                                1.dp,
                                if (taxResult.isProfitable) FutGreenProfit.copy(alpha = 0.8f) else FutRedTax.copy(alpha = 0.8f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Column {
                            // Tax deduction row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Impuesto EA deducido (5%):",
                                    fontSize = 11.sp,
                                    color = FutTextSecondary
                                )
                                Text(
                                    text = "-${formatCoins(taxResult.taxAmount)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FutRedTax
                                )
                            }

                            // Net proceeds credited to club
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Ingreso recibido tras tasa (95%):",
                                    fontSize = 11.sp,
                                    color = FutTextSecondary
                                )
                                Text(
                                    text = formatCoins(taxResult.proceedsAfterTax),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FutCyanAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FutBorder))
                            Spacer(modifier = Modifier.height(6.dp))

                            // Highlighted Net Profit / Loss
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (taxResult.isProfitable) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                        contentDescription = null,
                                        tint = if (taxResult.isProfitable) FutGreenProfit else FutRedTax,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (taxResult.isProfitable) "BENEFICIO NETO LIMPIO:" else "PÉRDIDA ESTIMADA:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = FutTextPrimary
                                    )
                                }

                                Text(
                                    text = "${if (taxResult.netProfit >= 0) "+" else ""}${formatCoins(taxResult.netProfit)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (taxResult.isProfitable) FutGreenProfit else FutRedTax
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Breakeven guidance note
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = FutGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Punto de equilibrio: Vende a mínimo ${formatCoins(taxResult.breakevenPrice)} para cubrir el 5% de EA.",
                            fontSize = 10.sp,
                            color = FutTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chemistry Style Multipliers
            Text(
                text = "VALORACIÓN CON ESTILOS DE QUÍMICA META",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FutGold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val shadowProfit = EaTaxCalculator.calculateNetProfit(futbinPrice, player.chemStyleShadowPriceConsole)
                val hunterProfit = EaTaxCalculator.calculateNetProfit(futbinPrice, player.chemStyleHunterPriceConsole)

                ChemBox(
                    name = "Sombra",
                    price = player.chemStyleShadowPriceConsole,
                    netProfit = shadowProfit,
                    onClick = {
                        sellInput = player.chemStyleShadowPriceConsole.toString()
                        selectedChem = "Sombra"
                    },
                    modifier = Modifier.weight(1f)
                )
                ChemBox(
                    name = "Cazador",
                    price = player.chemStyleHunterPriceConsole,
                    netProfit = hunterProfit,
                    onClick = {
                        sellInput = player.chemStyleHunterPriceConsole.toString()
                        selectedChem = "Cazador"
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            if (player.leakNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(FutCyanAccent.copy(alpha = 0.1f))
                        .border(1.dp, FutCyanAccent.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.FlashOn, contentDescription = null, tint = FutCyanAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "NOTICIA & LEAK FC 27", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FutCyanAccent)
                        }
                        Text(text = player.leakNotes, fontSize = 12.sp, color = FutTextPrimary, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        onAddToPortfolio(player, buyAmount, sellAmount, selectedChem)
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("save_to_portfolio_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = FutGreenProfit, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Guardar en Cartera", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        onAiConsultClick(player)
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("consult_ai_from_modal_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = FutGold, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Consultar IA", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PresetPriceChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(FutSurfaceElevated)
            .border(1.dp, FutBorder, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FutCyanAccent)
    }
}

@Composable
private fun ChemBox(
    name: String,
    price: Int,
    netProfit: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(FutSurfaceElevated)
            .border(1.dp, FutBorder, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column {
            Text(text = "Con $name", fontSize = 11.sp, color = FutTextSecondary)
            Text(text = formatCoins(price), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FutTextPrimary)
            Text(
                text = "Neto tras 5%: +${formatCoins(netProfit)}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (netProfit > 0) FutGreenProfit else FutTextMuted
            )
        }
    }
}
