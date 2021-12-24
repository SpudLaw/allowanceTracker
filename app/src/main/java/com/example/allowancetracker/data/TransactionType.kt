package com.example.allowancetracker.data


enum class TransactionType(
    val serializedName: String, //Name used to serialize to DB
    val isDeposit: Boolean //Denotes if this transaction type deposits or removes funds
) {
    InitialDeposit("InitialDeposit", true),
    Purchase("Purchase", false),
    Deposit("Deposit", true);

    companion object {
        val depositTypes = values().filter { it.isDeposit }

        init {
            //If we duplicated any transaction types,
            //in debug mode the app will crash at startup with this message
            assert(values() == values().distinct()) {
                "TransactionTypes must have different serialized names"
            }
        }
    }

}