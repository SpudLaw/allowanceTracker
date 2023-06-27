package com.example.allowancetracker.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "purchase_table")
data class Purchase(
    @ColumnInfo(name = "transaction_type") val type: TransactionType,
    @ColumnInfo(name = "cost") val cost: Double,
    @ColumnInfo(name = "date") val date: Date = Date(),
    @ColumnInfo(name = "description") val description: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable