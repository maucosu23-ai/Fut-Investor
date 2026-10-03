package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SensorsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Platform
import com.example.ui.theme.FutBorder
import com.example.ui.theme.FutGold
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

@Composable
fun FutTopBar(
    platform: Platform,
    onPlatformChange: (Platform) -> Unit,
    isRefreshing: Boolean,
    onRefreshClick: () -> Unit,
    autoRefreshEnabled: Boolean,
    onToggleAutoRefresh: () -> Unit,
    lastSyncTime: String,
    futbinLatency: Long,
    futggLatency: Long
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Surface(
        color = FutSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Main Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title and Live Status Indicator
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "FUT TRADER",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = FutGold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(FutGold.copy(alpha = 0.2f))
                                .padding(horizontal = 5.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "FC 27",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FutGold
                            )
                        }
                    }

                    // Live Subtitle with source badges
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(
                                    if (autoRefreshEnabled) FutGreenProfit.copy(alpha = pulseAlpha)
                                    else FutTextMuted
                                )
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (autoRefreshEnabled) "EN VIVO" else "PAUSADO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (autoRefreshEnabled) FutGreenProfit else FutTextMuted
                        )
                        Text(
                            text = " • $lastSyncTime",
                            fontSize = 10.sp,
                            color = FutTextSecondary
                        )
                    }
                }

                // Action Controls: Auto-refresh toggle & Manual refresh
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleAutoRefresh,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("toggle_auto_refresh_button")
                    ) {
                        Icon(
                            imageVector = if (autoRefreshEnabled) Icons.Default.Sensors else Icons.Default.SensorsOff,
                            contentDescription = "Alternar actualización en vivo",
                            tint = if (autoRefreshEnabled) FutGreenProfit else FutTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onRefreshClick,
                        enabled = !isRefreshing,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("manual_refresh_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refrescar precios ahora",
                            tint = FutGold,
                            modifier = Modifier
                                .size(20.dp)
                                .then(if (isRefreshing) Modifier.rotate(pulseAlpha * 360f) else Modifier)
                        )
                    }
                }
            }

            // Platform Switcher & Provider Latency Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Platform Segmented Switcher (PS/Xbox vs PC)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FutSurfaceVariant)
                        .border(1.dp, FutBorder, RoundedCornerShape(8.dp))
                        .padding(2.dp)
                ) {
                    PlatformSegment(
                        platform = Platform.CONSOLE,
                        selected = platform == Platform.CONSOLE,
                        icon = Icons.Default.Gamepad,
                        onClick = { onPlatformChange(Platform.CONSOLE) }
                    )
                    PlatformSegment(
                        platform = Platform.PC,
                        selected = platform == Platform.PC,
                        icon = Icons.Default.Laptop,
                        onClick = { onPlatformChange(Platform.PC) }
                    )
                }

                // Sources status chips (Futbin & FUT.GG)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SourceBadge(name = "Futbin", color = FutbinBrand, latency = futbinLatency)
                    SourceBadge(name = "FUT.GG", color = FutggBrand, latency = futggLatency)
                }
            }
        }
    }
}

@Composable
private fun PlatformSegment(
    platform: Platform,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (selected) FutGold else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = platform.label,
                tint = if (selected) Color.Black else FutTextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = platform.shortName,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color.Black else FutTextSecondary
            )
        }
    }
}

@Composable
private fun SourceBadge(name: String, color: Color, latency: Long) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(FutSurfaceElevated)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${latency}ms",
                fontSize = 9.sp,
                color = FutTextMuted
            )
        }
    }
}
