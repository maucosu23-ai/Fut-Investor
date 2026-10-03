package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val playerId: String,
    val playerName: String,
    val rating: Int,
    val position: String,
    val cardType: String,
    val alertPriceBelow: Int,
    val targetSellPrice: Int,
    val initialFutbinPrice: Int,
    val initialFutggPrice: Int,
    val addedAt: Long = System.currentTimeMillis()
)
