package com.example.allowancetracker.ui.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
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

    var sharedPref: SharedPreferences

    init {
        val purchaseDao = PurchaseDatabase.getDatabase(application, viewModelScope).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
        purchases = repository.allPurchases

        sharedPref = application.getSharedPreferences("preference_key",Context.MODE_PRIVATE)
        val defaultValue = 400.toFloat()
        val currentAllowance = sharedPref.getFloat("balance", defaultValue).toDouble()

        balance = MutableLiveData(currentAllowance)
    }

    fun add(purchase: Purchase) = viewModelScope.launch {
        repository.insert(purchase)

        with (sharedPref.edit()) {
            balance.value?.let { putFloat("balance", it.toFloat()) }
            apply()
        }
    }

    fun getBalanceString(): String {
        return String.format("$%.2f", balance.value)
    }
}