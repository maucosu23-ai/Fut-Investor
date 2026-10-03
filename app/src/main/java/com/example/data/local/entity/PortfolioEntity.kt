package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
data class PortfolioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playerId: String,
    val playerName: String,
    val rating: Int,
    val position: String,
    val cardType: String,
    val buyPrice: Int,
    val quantity: Int = 1,
    val targetSellPrice: Int,
    val chemStyle: String = "Básico",
    val isSold: Boolean = false,
    val soldPrice: Int = 0,
    val buyDate: Long = System.currentTimeMillis()
)
