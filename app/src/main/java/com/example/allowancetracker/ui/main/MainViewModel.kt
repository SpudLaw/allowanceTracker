package com.example.allowancetracker.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.data.PurchaseDatabase
import com.example.allowancetracker.data.PurchaseRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PurchaseRepository

    init {
        val purchaseDao = PurchaseDatabase.getDatabase(application, viewModelScope).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
    }

    var purchases = repository.allPurchases
    val balance = repository.balance

    val balanceString = balance.map { String.format("$%.2f", it) }


    fun add(purchase: Purchase) = viewModelScope.launch {
        repository.insert(purchase)
    }
}