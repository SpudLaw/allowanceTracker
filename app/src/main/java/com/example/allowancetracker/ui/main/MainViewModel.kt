package com.example.allowancetracker.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.data.PurchaseDatabase
import com.example.allowancetracker.data.PurchaseRepository
import com.example.allowancetracker.data.PurchaseType
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PurchaseRepository

    init {
        val purchaseDao = PurchaseDatabase.getDatabase(application, viewModelScope).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
    }

    var purchases = repository.allPurchases

    val balance = purchases.map {
        //Each time we get a new list of purchases
        //Starting with a 0.0 balance, loop through the list
        it.fold(0.0) { total, purchase ->
            when (purchase.type) {
                //If a purchase is a deposit, we add it to the total
                PurchaseType.InitialDeposit,
                PurchaseType.Deposit -> total + purchase.cost
                //If a purchase is a purchase, we remove it from the total
                PurchaseType.Purchase -> total - purchase.cost
            }
        }
    }

    val balanceString = balance.map { String.format("$%.2f", it) }


    fun add(purchase: Purchase) = viewModelScope.launch {
        repository.insert(purchase)
    }
}