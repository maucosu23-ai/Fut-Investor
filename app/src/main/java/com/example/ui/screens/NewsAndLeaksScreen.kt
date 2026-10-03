package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.AiAnalysisResult
import com.example.data.model.Fc27NewsItem
import com.example.data.model.NewsCategory
import com.example.data.model.PlayerItem
import com.example.data.model.TradingSignal
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsAndLeaksScreen(
    newsList: List<Fc27NewsItem>,
    aiAnalysis: AiAnalysisResult?,
    isAiAnalyzing: Boolean,
    onAiQuerySubmit: (String) -> Unit,
    onTrackTradeInPortfolio: (AiAnalysisResult) -> Unit,
    onAffectedPlayerClick: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<NewsCategory?>(null) }
    var userQueryText by remember { mutableStateOf("") }

    val presetPrompts = listOf(
        "¿Qué medias comprar antes del SBC de hoy?",
        "Tengo 100k, ¿qué jugador de promo comprar?",
        "¿Vendo a Haaland oro si entra al TOTW?",
        "Impacto de Trailblazers en medias 88"
    )

    val filteredNews = if (selectedCategory == null) newsList else newsList.filter { it.category == selectedCategory }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FutDarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Section 1: AI Interactive Advisor Bar
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // AI Prompt Input Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(FutSurfaceVariant)
                            .border(1.dp, FutGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(FutGold.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = FutGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Asesor de Inversión con IA (Gemini 3.5)",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FutGold
                                    )
                                    Text(
                                        text = "Pregunta sobre cualquier presupuesto, jugador o leak de FC 27",
                                        fontSize = 11.sp,
                                        color = FutTextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = userQueryText,
                                    onValueChange = { userQueryText = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("ai_custom_query_input"),
                                    placeholder = {
                                        Text(
                                            text = "Ej: Tengo 150k, ¿qué comprar antes de la promo?",
                                            fontSize = 12.sp,
                                            color = FutTextMuted
                                        )
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = FutSurface,
                                        unfocusedContainerColor = FutSurface,
                                        focusedBorderColor = FutGold,
                                        unfocusedBorderColor = FutBorder,
                                        focusedTextColor = FutTextPrimary,
                                        unfocusedTextColor = FutTextPrimary
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        if (userQueryText.isNotBlank()) {
                                            onAiQuerySubmit(userQueryText)
                                            userQueryText = ""
                                        }
                                    },
                                    enabled = !isAiAnalyzing && userQueryText.isNotBlank(),
                                    modifier = Modifier
                                        .height(52.dp)
                                        .testTag("ai_send_query_button"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = FutGold,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    if (isAiAnalyzing) {
                                        CircularProgressIndicator(
                                            color = Color.Black,
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = "Enviar a IA",
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            // Preset Prompt Pills
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                presetPrompts.forEach { prompt ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(FutSurfaceElevated)
                                            .border(1.dp, FutBorder, RoundedCornerShape(6.dp))
                                            .clickable { onAiQuerySubmit(prompt) }
                                            .padding(horizontal = 8.dp, vertical = 5.dp)
                                    ) {
                                        Text(
                                            text = prompt,
                                            fontSize = 11.sp,
                                            color = FutCyanAccent
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Active AI Recommendation Banner (if available)
            if (aiAnalysis != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        AiRecommendationCard(
                            analysis = aiAnalysis,
                            onTrackTrade = { onTrackTradeInPortfolio(aiAnalysis) }
                        )
                    }
                }
            }

            // Section 3: News Feed Filters Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = FutGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RADAR DE NOTICIAS & LEAKS FC 27",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = FutTextPrimary
                            )
                        }

                        Text(
                            text = "${filteredNews.size} noticias activas",
                            fontSize = 11.sp,
                            color = FutTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // News Category Filter Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("Todas las noticias", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FutGold,
                                containerColor = FutSurface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = if (selectedCategory == null) FutGold else FutBorder,
                                selectedBorderColor = FutGold,
                                enabled = true,
                                selected = selectedCategory == null
                            )
                        )

                        NewsCategory.values().forEach { cat ->
                            val isSelected = selectedCategory == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategory = if (isSelected) null else cat },
                                label = { Text(cat.label, fontSize = 12.sp) },
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
                }
            }

            // Section 4: News Articles List
            items(filteredNews, key = { it.id }) { news ->
                NewsArticleCard(
                    news = news,
                    onPlayerClick = onAffectedPlayerClick,
                    onConsultAiClick = {
                        onAiQuerySubmit("Analiza en profundidad el impacto de: ${news.title} para los jugadores ${news.affectedPlayerNames.joinToString(", ")}")
                    }
                )
            }
        }
    }
}

@Composable
private fun AiRecommendationCard(
    analysis: AiAnalysisResult,
    onTrackTrade: () -> Unit
) {
    val signalColor = Color(analysis.signal.badgeColorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ai_recommendation_embedded_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, signalColor.copy(alpha = 0.8f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Signal Badge & Target Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(signalColor.copy(alpha = 0.2f))
                            .border(1.dp, signalColor, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = analysis.signal.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = signalColor
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = analysis.queryOrPlayer,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutTextPrimary
                    )
                }

                Text(
                    text = analysis.timeHorizon,
                    fontSize = 11.sp,
                    color = FutTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing Matrix: Target Buy, Target Sell, Expected Net Profit & ROI
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
                        text = formatCoins(analysis.targetBuyPrice),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutGold
                    )
                }

                Column {
                    Text(text = "Vender a:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(
                        text = formatCoins(analysis.targetSellPrice),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutCyanAccent
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Neto tras 5% EA:", fontSize = 10.sp, color = FutTextSecondary)
                    Text(
                        text = "+${formatCoins(analysis.expectedNetProfit)} (${String.format(Locale.US, "%.1f", analysis.expectedRoi)}% ROI)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = FutGreenProfit
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Analysis Reasoning Text
            Text(
                text = analysis.analysisReasoning,
                fontSize = 12.sp,
                color = FutTextPrimary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onTrackTrade,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FutGold,
                        contentColor = Color.Black
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Registrar este trade en mi Cartera",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NewsArticleCard(
    news: Fc27NewsItem,
    onPlayerClick: (String) -> Unit,
    onConsultAiClick: () -> Unit
) {
    val signalColor = Color(news.recommendedSignal.badgeColorHex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("news_card_${news.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FutSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, FutBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Category & Date & Source
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(FutCyanAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = news.category.label.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutCyanAccent
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = news.source,
                        fontSize = 11.sp,
                        color = FutTextSecondary
                    )
                }

                Text(
                    text = news.dateDisplay,
                    fontSize = 10.sp,
                    color = FutTextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Article Title & Headline
            Text(
                text = news.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = FutTextPrimary,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = news.headline,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = FutGold,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = news.content,
                fontSize = 12.sp,
                color = FutTextPrimary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Affected Players Chips
            Text(
                text = "JUGADORES AFECTADOS:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = FutTextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                news.affectedPlayerNames.forEach { playerName ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(FutSurfaceElevated)
                            .border(1.dp, FutBorder, RoundedCornerShape(6.dp))
                            .clickable { onPlayerClick(playerName) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = playerName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutTextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Recommendation Box & Timing Windows
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FutSurface)
                    .border(1.dp, signalColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Acción Recomendada: ",
                                fontSize = 11.sp,
                                color = FutTextSecondary
                            )
                            Text(
                                text = news.recommendedSignal.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = signalColor
                            )
                        }

                        IconButton(
                            onClick = onConsultAiClick,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Profundizar con IA",
                                tint = FutGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "💡 ${news.tradingTip}",
                        fontSize = 11.sp,
                        color = FutGreenProfit,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🛒 Compra: ${news.buyTargetWindow}",
                            fontSize = 10.sp,
                            color = FutTextMuted
                        )
                        Text(
                            text = "🏷️ Venta: ${news.sellTargetWindow}",
                            fontSize = 10.sp,
                            color = FutTextMuted
                        )
                    }
                }
            }
        }
    }
}
