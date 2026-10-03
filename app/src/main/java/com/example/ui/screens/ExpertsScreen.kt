package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExpertConsensus
import com.example.data.model.ExpertProfile
import com.example.data.model.ExpertRecommendation
import com.example.data.model.PlayerItem
import com.example.ui.components.formatCoins
import com.example.ui.theme.FutBorder
import com.example.ui.theme.FutCyanAccent
import com.example.ui.theme.FutDarkBackground
import com.example.ui.theme.FutGold
import com.example.ui.theme.FutGreenProfit
import com.example.ui.theme.FutRedTax
import com.example.ui.theme.FutSurface
import com.example.ui.theme.FutSurfaceElevated
import com.example.ui.theme.FutSurfaceVariant
import com.example.ui.theme.FutTextMuted
import com.example.ui.theme.FutTextPrimary
import com.example.ui.theme.FutTextSecondary
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExpertsScreen(
    recommendations: List<ExpertRecommendation>,
    expertProfiles: List<ExpertProfile>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedTag: String?,
    onTagSelect: (String?) -> Unit,
    selectedProfile: ExpertProfile?,
    onProfileSelect: (ExpertProfile?) -> Unit,
    selectedConsensus: ExpertConsensus?,
    onViewConsensus: (PlayerItem) -> Unit,
    onDismissConsensus: () -> Unit,
    onUpvote: (String) -> Unit,
    onAddToPortfolio: (PlayerItem, Int, Int, String) -> Unit,
    onPlayerClick: (PlayerItem) -> Unit
) {
    val quickTags = listOf(
        "Promo Leak",
        "Medias Fodder",
        "Cross-Market",
        "Estilo Química",
        "Out of Packs",
        "Presupuesto Bajo"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FutDarkBackground)
    ) {
        // Search Input Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("expert_search_input"),
                placeholder = {
                    Text(
                        text = "Buscar por jugador, experto, estrategia o presupuesto...",
                        fontSize = 12.sp,
                        color = FutTextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = FutGold,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar",
                                tint = FutTextSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FutSurface,
                    unfocusedContainerColor = FutSurface,
                    focusedBorderColor = FutGold,
                    unfocusedBorderColor = FutBorder,
                    focusedTextColor = FutTextPrimary,
                    unfocusedTextColor = FutTextPrimary
                )
            )
        }

        // Horizontal Expert Profiles Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // "All Experts" card
            val isAllSelected = selectedProfile == null
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isAllSelected) FutSurfaceElevated else FutSurface)
                    .border(1.dp, if (isAllSelected) FutGold else FutBorder, RoundedCornerShape(10.dp))
                    .clickable { onProfileSelect(null) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = if (isAllSelected) FutGold else FutTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Todos los Expertos",
                        fontSize = 11.sp,
                        fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isAllSelected) FutGold else FutTextSecondary
                    )
                }
            }

            // Individual Expert Cards
            expertProfiles.forEach { expert ->
                val isSelected = selectedProfile?.id == expert.id
                val badgeColor = Color(expert.avatarBadgeHex)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) FutSurfaceElevated else FutSurface)
                        .border(
                            1.dp,
                            if (isSelected) badgeColor else FutBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { onProfileSelect(if (isSelected) null else expert) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(badgeColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = expert.name.take(1),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = badgeColor
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = expert.name,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) badgeColor else FutTextPrimary
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verificado",
                                    tint = badgeColor,
                                    modifier = Modifier.size(11.dp)
                                )
                            }
                            Text(
                                text = "${expert.accuracyPercent}% Aciertos",
                                fontSize = 9.sp,
                                color = FutGreenProfit
                            )
                        }
                    }
                }
            }
        }

        // Quick Tag Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            quickTags.forEach { tag ->
                val isSelected = selectedTag == tag
                FilterChip(
                    selected = isSelected,
                    onClick = { onTagSelect(if (isSelected) null else tag) },
                    label = { Text(tag, fontSize = 11.sp) },
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

        // Active Consensus Modal/Card (if opened)
        if (selectedConsensus != null) {
            ConsensusViewCard(
                consensus = selectedConsensus,
                onDismiss = onDismissConsensus,
                onAddToPortfolio = onAddToPortfolio
            )
        }

        // Recommendations Feed
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            if (recommendations.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = FutTextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Sin recomendaciones para esta búsqueda",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutTextPrimary
                            )
                            Text(
                                text = "Prueba a seleccionar otro experto o quitar los filtros",
                                fontSize = 12.sp,
                                color = FutTextSecondary
                            )
                        }
                    }
                }
            } else {
                items(recommendations, key = { it.id }) { rec ->
                    ExpertRecommendationCard(
                        rec = rec,
                        onViewConsensus = { onViewConsensus(rec.player) },
                        onUpvote = { onUpvote(rec.id) },
                        onAddToPortfolio = {
                            onAddToPortfolio(
                                rec.player,
                                rec.targetBuyPrice,
                                rec.targetSellPrice,
                                "Recomendación ${rec.expert.name}"
                            )
                        },
                        onPlayerClick = { onPlayerClick(rec.player) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpertRecommendationCard(
    rec: ExpertRecommendation,
    onViewConsensus: () -> Unit,
    onUpvote: () -> Unit,
    onAddToPortfolio: () -> Unit,
    onPlayerClick: () -> Unit
) {
    val badgeColor = Color(rec.expert.avatarBadgeHex)
    val signalColor = Color(rec.signal.badgeColorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("expert_rec_card_${rec.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, FutBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Expert Header: Avatar, Name, Handle, Accuracy, Timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(badgeColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = rec.expert.name.take(1),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = badgeColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = rec.expert.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutTextPrimary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = badgeColor,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = "${rec.expert.handle} • ${rec.expert.specialty}",
                            fontSize = 10.sp,
                            color = FutTextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(FutGreenProfit.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${rec.expert.accuracyPercent}% Acierto",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutGreenProfit
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Player Info & Signal Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onPlayerClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${rec.player.rating}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = FutGold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = rec.player.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutTextPrimary
                        )
                        Text(
                            text = "${rec.player.club} • ${rec.player.cardType}",
                            fontSize = 11.sp,
                            color = FutTextSecondary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(signalColor.copy(alpha = 0.2f))
                        .border(1.dp, signalColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = rec.signal.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = signalColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Investment Matrix: Buy Target, Sell Target, Tax, Net Profit & ROI
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(FutSurface)
                    .border(1.dp, FutBorder, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Comprar a:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(
                        text = formatCoins(rec.targetBuyPrice),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutGold
                    )
                    Text(
                        text = "Vender a: ${formatCoins(rec.targetSellPrice)}",
                        fontSize = 9.sp,
                        color = FutTextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Tasa EA 5%:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(
                        text = "-${formatCoins(rec.eaTax)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutRedTax
                    )
                    Text(
                        text = rec.timeHorizon,
                        fontSize = 9.sp,
                        color = FutCyanAccent
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Beneficio Neto:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(
                        text = "+${formatCoins(rec.netProfit)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = FutGreenProfit
                    )
                    Text(
                        text = "+${String.format(Locale.US, "%.1f", rec.roiPercent)}% ROI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutGreenProfit
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expert Thesis & Reasoning
            Text(
                text = "💬 \"${rec.thesis}\"",
                fontSize = 12.sp,
                color = FutTextPrimary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tags row
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                rec.tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(FutSurfaceElevated)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "#$tag", fontSize = 10.sp, color = FutTextMuted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions Row: Upvote, Consensus & Follow Trade
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(FutSurfaceElevated)
                        .clickable(onClick = onUpvote)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Votar",
                        tint = FutGold,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${rec.upvotes}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutTextPrimary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onViewConsensus,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FutCyanAccent),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FutCyanAccent.copy(alpha = 0.5f))
                    ) {
                        Text(text = "Consenso", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onAddToPortfolio,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FutGreenProfit,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Seguir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsensusViewCard(
    consensus: ExpertConsensus,
    onDismiss: () -> Unit,
    onAddToPortfolio: (PlayerItem, Int, Int, String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("consensus_modal_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, FutGold)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "CONSENSO DE EXPERTOS: ${consensus.player.name.uppercase()}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = FutGold
                    )
                    Text(
                        text = "${consensus.consensusPercent}% de analistas recomiendan ${consensus.overallSignal.label}",
                        fontSize = 11.sp,
                        color = FutGreenProfit
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Cerrar",
                        tint = FutTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Voting meter bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(FutGreenProfit.copy(alpha = 0.2f))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "COMPRAR (${consensus.buyVotes})", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FutGreenProfit)
                }
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(FutGold.copy(alpha = 0.2f))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "MANTENER (${consensus.holdVotes})", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FutGold)
                }
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .clip(RoundedCornerShape(6.dp))
                        .background(FutRedTax.copy(alpha = 0.2f))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "VENDER (${consensus.sellVotes})", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FutRedTax)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Average Consensus Targets
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(FutSurface)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Compra Promedio:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(text = formatCoins(consensus.avgBuyTarget), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FutGold)
                }
                Column {
                    Text(text = "Venta Promedio:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(text = formatCoins(consensus.avgSellTarget), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = FutCyanAccent)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Neto tras 5% EA:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(text = "+${formatCoins(consensus.avgNetProfit)}", fontSize = 12.sp, fontWeight = FontWeight.Black, color = FutGreenProfit)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expert Quotes Side-by-Side
            Text(text = "ARGUMENTOS DE LOS ANALISTAS:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = FutTextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            consensus.expertTheses.forEach { (exp, thesis) ->
                Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
                    Text(text = "${exp.name}: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(exp.avatarBadgeHex))
                    Text(text = thesis, fontSize = 11.sp, color = FutTextPrimary, lineHeight = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    onAddToPortfolio(consensus.player, consensus.avgBuyTarget, consensus.avgSellTarget, "Consenso Expertos")
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().height(42.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FutGold, contentColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Seguir trade según el consenso", fontWeight = FontWeight.Bold)
            }
        }
    }
}
