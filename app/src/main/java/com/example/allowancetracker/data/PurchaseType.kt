package com.example.allowancetracker.data


enum class PurchaseType(
    val isDeposit: Boolean //Deposit types add to the balance
) {
    InitialDeposit(true),
    Purchase(false),
    Deposit(true);

    companion object {
        val depositTypes = values().filter { it.isDeposit }
    }
}