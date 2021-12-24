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
    fun toTransactionType(transactionType: TransactionType): String = transactionType.serializedName

    @TypeConverter
    fun fromTransactionType(string: String): TransactionType = TransactionType.values()
                                                            .first { it.serializedName == string }

}