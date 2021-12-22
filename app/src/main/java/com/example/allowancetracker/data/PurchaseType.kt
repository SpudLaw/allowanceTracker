package com.example.allowancetracker.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*


enum class PurchaseType{
    InitialDeposit,
    Purchase,
    Deposit
}