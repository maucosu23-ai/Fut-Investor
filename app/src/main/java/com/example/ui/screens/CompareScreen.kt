package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Platform
import com.example.data.model.PlayerItem
import com.example.ui.components.PlayerPriceCard
import com.example.ui.theme.FutBorder
import com.example.ui.theme.FutDarkBackground
import com.example.ui.theme.FutGold
import com.example.ui.theme.FutSurface
import com.example.ui.theme.FutTextMuted
import com.example.ui.theme.FutTextPrimary
import com.example.ui.theme.FutTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    players: List<PlayerItem>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    platform: Platform,
    watchlistPlayerIds: Set<String>,
    onWatchlistToggle: (PlayerItem) -> Unit,
    onPlayerClick: (PlayerItem) -> Unit,
    onAiConsultClick: (PlayerItem) -> Unit,
    onCalcClick: (PlayerItem) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val categories = listOf("Todos", "Meta Oro", "Promos & TOTW", "Medias Fodder (84-90)")
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize()
    ) {
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
                        .testTag("search_player_input"),
                    placeholder = {
                        Text(
                            text = "Buscar jugador, club, liga o posición...",
                            fontSize = 13.sp,
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

            // Category Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategoryChange(category) },
                        label = {
                            Text(
                                text = category,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else FutTextSecondary
                            )
                        },
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

            Spacer(modifier = Modifier.height(6.dp))

            // Player List
            if (players.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = FutTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No se encontraron jugadores",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = FutTextPrimary
                        )
                        Text(
                            text = "Prueba con otro nombre, filtro o categoría",
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
                    items(players, key = { it.id }) { player ->
                        PlayerPriceCard(
                            player = player,
                            platform = platform,
                            isInWatchlist = watchlistPlayerIds.contains(player.id),
                            onWatchlistToggle = { onWatchlistToggle(player) },
                            onAiConsultClick = { onAiConsultClick(player) },
                            onCardClick = { onPlayerClick(player) },
                            onCalcClick = { onCalcClick(player) }
                        )
                    }
                }
            }
        }
    }
}
