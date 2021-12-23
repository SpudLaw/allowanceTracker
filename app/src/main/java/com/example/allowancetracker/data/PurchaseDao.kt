package com.example.allowancetracker.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PurchaseDao {
    @Query("SELECT * from purchase_table ORDER BY date ASC")
    fun getAll(): LiveData<List<Purchase>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(vararg purchase: Purchase)

    @Query("DELETE FROM purchase_table")
    suspend fun deleteAll()
}