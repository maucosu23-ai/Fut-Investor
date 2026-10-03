package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.entity.PortfolioEntity
import com.example.data.local.entity.WatchlistEntity
import com.example.ui.components.formatCoins
import com.example.ui.theme.FutBorder
import com.example.ui.theme.FutCyanAccent
import com.example.ui.theme.FutDarkBackground
import com.example.ui.theme.FutGold
import com.example.ui.theme.FutGreenDark
import com.example.ui.theme.FutGreenProfit
import com.example.ui.theme.FutRedTax
import com.example.ui.theme.FutSurface
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.example.ui.theme.FutSurfaceElevated
import com.example.ui.theme.FutSurfaceVariant
import com.example.ui.theme.FutTextMuted
import com.example.ui.theme.FutTextPrimary
import com.example.ui.theme.FutTextSecondary
import java.util.Locale

@Composable
fun CalculatorAndPortfolioScreen(
    calcBuyInput: String,
    calcSellInput: String,
    onCalcInputsChange: (String, String) -> Unit,
    portfolioList: List<PortfolioEntity>,
    watchlist: List<WatchlistEntity>,
    onMarkSold: (Long, Int) -> Unit
) {
    var selectedSubTab by remember { mutableIntStateOf(0) } // 0 = Calculadora 5%, 1 = Cartera / Flips, 2 = Radar
    var showSoldDialog by remember { mutableStateOf<PortfolioEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FutDarkBackground)
    ) {
        // Tab Selector Row
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = FutSurface,
            contentColor = FutGold,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedSubTab])
                        .height(3.dp)
                        .background(FutGold)
                )
            }
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text("Calculadora 5% EA", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text("Mi Cartera (${portfolioList.count { !it.isSold }})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                text = { Text("Radar (${watchlist.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
        }

        when (selectedSubTab) {
            0 -> EaTaxCalculatorView(
                buyInput = calcBuyInput,
                sellInput = calcSellInput,
                onInputsChange = onCalcInputsChange
            )
            1 -> PortfolioTrackerView(
                portfolio = portfolioList,
                onMarkSoldClick = { showSoldDialog = it }
            )
            2 -> WatchlistView(watchlist = watchlist)
        }
    }

    // Dialog to mark a flip as sold and calculate real profit
    if (showSoldDialog != null) {
        val item = showSoldDialog!!
        var finalSellPriceInput by remember { mutableStateOf(item.targetSellPrice.toString()) }

        AlertDialog(
            onDismissRequest = { showSoldDialog = null },
            containerColor = FutSurface,
            title = {
                Text(
                    text = "Confirmar Venta de ${item.playerName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = FutTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Precio de compra: ${formatCoins(item.buyPrice)}",
                        fontSize = 13.sp,
                        color = FutTextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = finalSellPriceInput,
                        onValueChange = { finalSellPriceInput = it },
                        label = { Text("Precio Final de Venta") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FutGreenProfit,
                            unfocusedBorderColor = FutBorder,
                            focusedTextColor = FutTextPrimary,
                            unfocusedTextColor = FutTextPrimary
                        )
                    )
                    val sPrice = finalSellPriceInput.toIntOrNull() ?: 0
                    val tax = (sPrice * 0.05).toInt()
                    val net = (sPrice * 0.95).toInt() - item.buyPrice
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Tasa EA 5%: -${formatCoins(tax)}",
                        fontSize = 12.sp,
                        color = FutRedTax
                    )
                    Text(
                        text = "Beneficio Neto Real: +${formatCoins(net)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (net >= 0) FutGreenProfit else FutRedTax
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val sPrice = finalSellPriceInput.toIntOrNull() ?: item.targetSellPrice
                        onMarkSold(item.id, sPrice)
                        showSoldDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = FutGreenProfit, contentColor = Color.Black)
                ) {
                    Text("Confirmar Venta", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSoldDialog = null }) {
                    Text("Cancelar", color = FutTextSecondary)
                }
            }
        )
    }
}

@Composable
private fun EaTaxCalculatorView(
    buyInput: String,
    sellInput: String,
    onInputsChange: (String, String) -> Unit
) {
    val buyPrice = buyInput.toIntOrNull() ?: 0
    val sellPrice = sellInput.toIntOrNull() ?: 0

    val eaTax = (sellPrice * 0.05).toInt()
    val netProceeds = (sellPrice * 0.95).toInt()
    val netProfit = netProceeds - buyPrice
    val roi = if (buyPrice > 0) (netProfit.toFloat() / buyPrice) * 100f else 0f
    val breakevenPrice = kotlin.math.ceil(buyPrice / 0.95).toInt()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, FutBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = FutGold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "CALCULADORA DE TASA EA SPORTS 5%",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = FutGold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buy Price Input
                    OutlinedTextField(
                        value = buyInput,
                        onValueChange = { onInputsChange(it, sellInput) },
                        label = { Text("Precio de Compra (Monedas)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("calc_buy_price_input"),
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

                    // Quick increment buttons for buy price
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(1000, 5000, 20000, 50000).forEach { inc ->
                            QuickAddChip(label = "+${inc / 1000}k") {
                                val current = buyInput.toIntOrNull() ?: 0
                                onInputsChange((current + inc).toString(), sellInput)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sell Price Input
                    OutlinedTextField(
                        value = sellInput,
                        onValueChange = { onInputsChange(buyInput, it) },
                        label = { Text("Precio Objetivo de Venta (Monedas)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("calc_sell_price_input"),
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

                    // Quick increment buttons for sell price
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(1000, 5000, 20000, 50000).forEach { inc ->
                            QuickAddChip(label = "+${inc / 1000}k") {
                                val current = sellInput.toIntOrNull() ?: 0
                                onInputsChange(buyInput, (current + inc).toString())
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Results Breakdown Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (netProfit >= 0) FutGreenProfit.copy(alpha = 0.6f) else FutRedTax.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "DESGLOSE DE BENEFICIOS TRAS EL 5% DE EA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutTextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ResultRow(label = "Venta Bruta:", value = formatCoins(sellPrice), color = FutTextPrimary)
                    ResultRow(label = "Impuesto EA (5%):", value = "-${formatCoins(eaTax)}", color = FutRedTax)
                    ResultRow(label = "Ingreso Neto:", value = formatCoins(netProceeds), color = FutCyanAccent)
                    ResultRow(label = "Coste de Compra:", value = "-${formatCoins(buyPrice)}", color = FutTextSecondary)

                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(FutBorder))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Final Net Profit Highlight
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "BENEFICIO LIMPIO:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = FutTextPrimary
                            )
                            Text(
                                text = "ROI: ${String.format(Locale.US, "%.1f", roi)}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (netProfit >= 0) FutGreenProfit else FutRedTax
                            )
                        }

                        Text(
                            text = "${if (netProfit >= 0) "+" else ""}${formatCoins(netProfit)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = if (netProfit >= 0) FutGreenProfit else FutRedTax
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Breakeven Indicator Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(FutSurface)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "PUNTO DE EQUILIBRIO (BREAKEVEN):",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutGold
                            )
                            Text(
                                text = "Debes vender como mínimo a ${formatCoins(breakevenPrice)} para no perder monedas tras el impuesto de EA.",
                                fontSize = 12.sp,
                                color = FutTextPrimary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PortfolioTrackerView(
    portfolio: List<PortfolioEntity>,
    onMarkSoldClick: (PortfolioEntity) -> Unit
) {
    val activeFlips = portfolio.filter { !it.isSold }
    val soldFlips = portfolio.filter { it.isSold }

    val totalInvested = activeFlips.sumOf { it.buyPrice * it.quantity }
    val projectedNet = activeFlips.sumOf { ((it.targetSellPrice * 0.95).toInt() - it.buyPrice) * it.quantity }
    val realizedNet = soldFlips.sumOf { ((it.soldPrice * 0.95).toInt() - it.buyPrice) * it.quantity }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            // Portfolio KPI Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                KpiSmall(label = "Invertido", value = formatCoins(totalInvested), color = FutGold, modifier = Modifier.weight(1f))
                KpiSmall(label = "Proyectado", value = "+${formatCoins(projectedNet)}", color = FutCyanAccent, modifier = Modifier.weight(1f))
                KpiSmall(label = "Realizado", value = "+${formatCoins(realizedNet)}", color = FutGreenProfit, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (portfolio.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Inventory, contentDescription = null, tint = FutTextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Tu cartera está vacía", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FutTextPrimary)
                        Text(text = "Ve al comparador o al arbitraje para registrar tus compras", fontSize = 12.sp, color = FutTextSecondary)
                    }
                }
            }
        } else {
            item {
                Text(text = "CARTAS ACTIVAS EN VENTA (${activeFlips.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FutGold)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(activeFlips, key = { it.id }) { item ->
                val expectedNet = (item.targetSellPrice * 0.95).toInt() - item.buyPrice
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "${item.rating} ${item.playerName}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FutTextPrimary)
                            Text(text = "Comprado: ${formatCoins(item.buyPrice)} • Química: ${item.chemStyle}", fontSize = 11.sp, color = FutTextSecondary)
                            Text(text = "Objetivo Venta: ${formatCoins(item.targetSellPrice)}", fontSize = 11.sp, color = FutGold)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "+${formatCoins(expectedNet)}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = FutGreenProfit)
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = { onMarkSoldClick(item) },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = FutGreenProfit, contentColor = Color.Black)
                            ) {
                                Text("Vendido", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            if (soldFlips.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "HISTORIAL DE VENTAS COMPLETADAS (${soldFlips.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FutTextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(soldFlips, key = { it.id }) { item ->
                    val realizedProfit = (item.soldPrice * 0.95).toInt() - item.buyPrice
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = FutSurface)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${item.rating} ${item.playerName} (VENDIDO)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = FutTextSecondary)
                                Text(text = "Compra: ${formatCoins(item.buyPrice)} ➔ Venta: ${formatCoins(item.soldPrice)}", fontSize = 11.sp, color = FutTextMuted)
                            }
                            Text(text = "+${formatCoins(realizedProfit)}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = FutGreenProfit)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WatchlistView(watchlist: List<WatchlistEntity>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        if (watchlist.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Bookmark, contentDescription = null, tint = FutTextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Radar de Precios vacío", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = FutTextPrimary)
                        Text(text = "Toca el icono de marcador en cualquier jugador para seguirlo", fontSize = 12.sp, color = FutTextSecondary)
                    }
                }
            }
        } else {
            items(watchlist, key = { it.playerId }) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "${item.rating} ${item.playerName} (${item.position})", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = FutTextPrimary)
                            Text(text = "Futbin inicial: ${formatCoins(item.initialFutbinPrice)} • FUT.GG: ${formatCoins(item.initialFutggPrice)}", fontSize = 11.sp, color = FutTextSecondary)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Alerta bajo:", fontSize = 10.sp, color = FutTextMuted)
                            Text(text = formatCoins(item.alertPriceBelow), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FutGold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = FutTextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
private fun QuickAddChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(FutSurfaceElevated)
            .border(1.dp, FutBorder, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = FutGold)
    }
}

@Composable
private fun KpiSmall(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(FutSurface)
            .border(1.dp, FutBorder, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(text = label, fontSize = 10.sp, color = FutTextSecondary)
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Black, color = color, maxLines = 1)
        }
    }
}
