package com.example.allowancetracker.data

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toPurchaseType(purchaseType: PurchaseType): Int = purchaseType.ordinal

    @TypeConverter
    fun fromPurchasetype(int: Int): PurchaseType = PurchaseType.values()[int]

}