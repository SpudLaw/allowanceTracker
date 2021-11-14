package com.example.allowancetracker.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.data.PurchaseDatabase
import com.example.allowancetracker.data.PurchaseRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PurchaseRepository
    var balance: MutableLiveData<Double>
    var purchases: LiveData<List<Purchase>>

    init {
        val purchaseDao = PurchaseDatabase.getDatabase(application, viewModelScope).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
        purchases = repository.allPurchases

        balance = MutableLiveData(400.0)
    }

    fun add(purchase: Purchase) = viewModelScope.launch {
        repository.insert(purchase)
    }

    fun getBalanceString(): String {
        return String.format("$%.2f", balance.value)
    }
}