package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.ArbitrageOpportunity
import com.example.data.model.ArbitrageType
import com.example.data.model.Platform
import com.example.data.model.PlayerItem
import com.example.ui.components.formatCoins
import com.example.ui.theme.FutBorder
import com.example.ui.theme.FutCyanAccent
import com.example.ui.theme.FutDarkBackground
import com.example.ui.theme.FutGold
import com.example.ui.theme.FutGreenDark
import com.example.ui.theme.FutGreenProfit
import com.example.ui.theme.FutRedTax
import com.example.ui.theme.FutSurface
import com.example.ui.theme.FutSurfaceElevated
import com.example.ui.theme.FutSurfaceVariant
import com.example.ui.theme.FutTextMuted
import com.example.ui.theme.FutTextPrimary
import com.example.ui.theme.FutTextSecondary
import java.util.Locale

@Composable
fun ArbitrageScreen(
    opportunities: List<ArbitrageOpportunity>,
    selectedTypeFilter: ArbitrageType?,
    onTypeFilterChange: (ArbitrageType?) -> Unit,
    budgetFilterCoins: Int,
    onBudgetFilterChange: (Int) -> Unit,
    platform: Platform,
    onAiConsultClick: (PlayerItem) -> Unit,
    onAddToPortfolio: (PlayerItem, Int, Int, String) -> Unit,
    onPlayerClick: (PlayerItem) -> Unit
) {
    val budgetOptions = listOf(
        Pair("Todo", 10000000),
        Pair("< 25k", 25000),
        Pair("< 100k", 100000),
        Pair("< 500k", 500000)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FutDarkBackground)
    ) {
        // Summary KPI Banner
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KpiCard(
                label = "Oportunidades",
                value = "${opportunities.size}",
                sublabel = "Detectadas",
                color = FutGold,
                modifier = Modifier.weight(1f)
            )
            val maxRoi = opportunities.maxOfOrNull { it.roiPercent } ?: 0f
            KpiCard(
                label = "Mejor ROI",
                value = "+${String.format(Locale.US, "%.1f", maxRoi)}%",
                sublabel = "Tras 5% EA",
                color = FutGreenProfit,
                modifier = Modifier.weight(1f)
            )
            val totalProfit = opportunities.sumOf { it.netProfit }
            KpiCard(
                label = "Potencial",
                value = formatCoins(totalProfit),
                sublabel = "Monedas Netas",
                color = FutCyanAccent,
                modifier = Modifier.weight(1.2f)
            )
        }

        // Arbitrage Strategy Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedTypeFilter == null,
                onClick = { onTypeFilterChange(null) },
                label = { Text("Todos los Métodos", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = FutGold,
                    containerColor = FutSurface
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = if (selectedTypeFilter == null) FutGold else FutBorder,
                    selectedBorderColor = FutGold,
                    enabled = true,
                    selected = selectedTypeFilter == null
                )
            )

            ArbitrageType.values().forEach { type ->
                val isSelected = selectedTypeFilter == type
                FilterChip(
                    selected = isSelected,
                    onClick = { onTypeFilterChange(if (isSelected) null else type) },
                    label = { Text(type.title, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = FutGold,
                        containerColor = FutSurface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) FutGold else FutBorder,
                        selectedBorderColor = FutGold,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        // Budget Quick Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Presupuesto: ",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = FutTextSecondary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                budgetOptions.forEach { (label, maxCoins) ->
                    val isSelected = budgetFilterCoins == maxCoins
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) FutSurfaceElevated else FutSurface)
                            .border(
                                1.dp,
                                if (isSelected) FutGold else FutBorder,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { onBudgetFilterChange(maxCoins) }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) FutGold else FutTextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Arbitrage List
        if (opportunities.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.OfflineBolt,
                        contentDescription = null,
                        tint = FutGold,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Escaneando métodos de arbitraje...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutTextPrimary
                    )
                    Text(
                        text = "Ajusta el filtro de presupuesto o cambia de categoría",
                        fontSize = 13.sp,
                        color = FutTextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp)
            ) {
                items(opportunities, key = { it.id }) { opp ->
                    ArbitrageOpportunityCard(
                        opp = opp,
                        platform = platform,
                        onAiConsultClick = { onAiConsultClick(opp.player) },
                        onAddToPortfolio = {
                            onAddToPortfolio(
                                opp.player,
                                opp.buyTargetPrice,
                                opp.sellTargetPrice,
                                if (opp.type == ArbitrageType.CHEM_STYLE) "Sombra/Cazador" else "Básico"
                            )
                        },
                        onPlayerClick = { onPlayerClick(opp.player) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ArbitrageOpportunityCard(
    opp: ArbitrageOpportunity,
    platform: Platform,
    onAiConsultClick: () -> Unit,
    onAddToPortfolio: () -> Unit,
    onPlayerClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("arb_card_${opp.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (opp.isHotSignal) FutGreenProfit.copy(alpha = 0.6f) else FutBorder
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Strategy Type Badge & Hot Signal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (opp.isHotSignal) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Oportunidad Caliente",
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = opp.type.title.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = if (opp.isHotSignal) FutGreenProfit else FutGold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = FutTextMuted,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = opp.timeHorizon,
                        fontSize = 11.sp,
                        color = FutTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Player Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onPlayerClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${opp.player.rating}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = FutGold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = opp.player.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(${opp.player.position})",
                            fontSize = 12.sp,
                            color = FutTextSecondary
                        )
                    }
                    Text(
                        text = "${opp.player.club} • ${opp.player.cardType}",
                        fontSize = 11.sp,
                        color = FutTextSecondary
                    )
                }

                // Confidence badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(FutSurfaceElevated)
                        .border(1.dp, FutBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${opp.confidencePercent}% Fiabilidad",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutGreenProfit
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Strategy Summary text
            Text(
                text = opp.strategySummary,
                fontSize = 12.sp,
                color = FutTextPrimary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Numbers Matrix: Compra, Venta, Tasa EA 5%, Ganancia Neta & ROI
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
                Column {
                    Text(text = "Comprar a:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(
                        text = formatCoins(opp.buyTargetPrice),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutTextPrimary
                    )
                    Text(
                        text = "Vender a: ${formatCoins(opp.sellTargetPrice)}",
                        fontSize = 10.sp,
                        color = FutTextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Tasa EA (5%):", fontSize = 10.sp, color = FutTextSecondary)
                    Text(
                        text = "-${formatCoins(opp.eaTax)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutRedTax
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Beneficio Neto:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(
                        text = "+${formatCoins(opp.netProfit)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = FutGreenProfit
                    )
                    Text(
                        text = "ROI +${String.format(Locale.US, "%.1f", opp.roiPercent)}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutGreenProfit
                    )
                }
            }

            // Expandable Execution Steps
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(FutSurfaceElevated)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "PASO A PASO PARA EJECUTAR EL ARBITRAJE:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutGold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    opp.executionSteps.forEachIndexed { i, step ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "${i + 1}. ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutCyanAccent
                            )
                            Text(
                                text = step,
                                fontSize = 11.sp,
                                color = FutTextPrimary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Card Footer Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { expanded = !expanded },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (expanded) "Ocultar guía" else "Ver guía paso a paso",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutCyanAccent
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = FutCyanAccent,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onAiConsultClick,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FutGold),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FutGold)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "IA", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onAddToPortfolio,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FutGreenProfit, contentColor = Color.Black)
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Seguir Trade", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiCard(
    label: String,
    value: String,
    sublabel: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(FutSurface)
            .border(1.dp, FutBorder, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column {
            Text(text = label, fontSize = 10.sp, color = FutTextSecondary)
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = color,
                maxLines = 1
            )
            Text(text = sublabel, fontSize = 9.sp, color = FutTextMuted)
        }
    }
}
