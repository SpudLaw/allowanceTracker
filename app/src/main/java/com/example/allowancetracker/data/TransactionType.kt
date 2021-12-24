package com.example.allowancetracker.data


enum class TransactionType(
    val serializedName: String, //Name used to serialize to DB
    val isDeposit: Boolean //Name used to serialize to DB, val isDeposit: kotlin.Boolean){}
) {
    InitialDeposit("InitialDeposit", true),
    Purchase("Purchase", false),
    Deposit("Deposit", true);

    companion object {
        val depositTypes = values().filter { it.isDeposit }
    }
}