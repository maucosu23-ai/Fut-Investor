package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiAnalysisResult
import com.example.ui.theme.FutBorder
import com.example.ui.theme.FutCyanAccent
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAdvisorModal(
    analysis: AiAnalysisResult?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onTrackTradeClick: (AiAnalysisResult) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FutSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header: AI Title, Icon, Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(FutGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = FutGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Asesor de Trading con IA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = FutTextPrimary
                        )
                        Text(
                            text = "Motor Gemini 3.5 • EA FC 27 Market Brain",
                            fontSize = 11.sp,
                            color = FutGold
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = FutTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = FutGold, strokeWidth = 3.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Analizando leaks de FC 27, precios Futbin/FUT.GG y deducción de tasa EA...",
                        fontSize = 13.sp,
                        color = FutTextSecondary
                    )
                }
            } else if (analysis != null) {
                // Signal Banner (COMPRA FUERTE, COMPRAR, MANTENER, VENDER)
                val badgeColor = Color(analysis.signal.badgeColorHex)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .border(1.dp, badgeColor, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "VEREDICTO IA PARA ${analysis.queryOrPlayer.uppercase()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutTextSecondary
                            )
                            Text(
                                text = analysis.signal.label,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = badgeColor
                            )
                            Text(
                                text = "Horizonte: ${analysis.timeHorizon}",
                                fontSize = 11.sp,
                                color = FutTextPrimary
                            )
                        }

                        // Risk Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(analysis.riskLevel.colorHex).copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = analysis.riskLevel.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(analysis.riskLevel.colorHex)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Price Targets Grid: Target Buy, Target Sell, Expected Profit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TargetCard(
                        title = "Compra Objetivo",
                        amount = formatCoins(analysis.targetBuyPrice),
                        color = FutGold,
                        modifier = Modifier.weight(1f)
                    )
                    TargetCard(
                        title = "Venta Objetivo",
                        amount = formatCoins(analysis.targetSellPrice),
                        color = FutCyanAccent,
                        modifier = Modifier.weight(1f)
                    )
                    TargetCard(
                        title = "Beneficio Neto",
                        amount = "+${formatCoins(analysis.expectedNetProfit)}",
                        color = FutGreenProfit,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // EA 5% Tax Note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(FutSurfaceVariant)
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = FutRedTax,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = analysis.eaTaxBreakdown,
                            fontSize = 11.sp,
                            color = FutTextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Deep Market Reasoning
                Text(
                    text = "ANÁLISIS DE MERCADO & LEAKS FC 27",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FutGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = analysis.analysisReasoning,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = FutTextPrimary
                )

                if (analysis.promoImpactFactor.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Impacto de Promo / Filtración:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FutCyanAccent
                    )
                    Text(
                        text = analysis.promoImpactFactor,
                        fontSize = 12.sp,
                        color = FutTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Checklist
                Text(
                    text = "PASOS DE EJECUCIÓN RECOMENDADOS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = FutGold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    analysis.actionChecklist.forEachIndexed { index, step ->
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = FutGreenProfit,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = step,
                                fontSize = 12.sp,
                                color = FutTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onTrackTradeClick(analysis) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("track_trade_in_portfolio_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FutGold,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Registrar este trade en mi Cartera",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TargetCard(
    title: String,
    amount: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(FutSurfaceElevated)
            .border(1.dp, FutBorder, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 10.sp,
                color = FutTextSecondary
            )
            Text(
                text = amount,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                maxLines = 1
            )
        }
    }
}
