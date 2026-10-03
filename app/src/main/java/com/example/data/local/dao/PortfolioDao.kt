package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.PortfolioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio ORDER BY buyDate DESC")
    fun getAllPortfolio(): Flow<List<PortfolioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PortfolioEntity): Long

    @Update
    suspend fun update(item: PortfolioEntity)

    @Delete
    suspend fun delete(item: PortfolioEntity)

    @Query("UPDATE portfolio SET isSold = 1, soldPrice = :soldPrice WHERE id = :id")
    suspend fun markAsSold(id: Long, soldPrice: Int)
}
