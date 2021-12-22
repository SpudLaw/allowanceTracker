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
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PurchaseRepository
    var balance: MutableLiveData<Double>
    var purchases: LiveData<List<Purchase>>

    private var sharedPref: SharedPreferences

    init {
        val purchaseDao = PurchaseDatabase.getDatabase(application, viewModelScope).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
        purchases = repository.allPurchases

        sharedPref = application.getSharedPreferences("preference_key", Context.MODE_PRIVATE)
        val defaultValue = 400.toFloat()
        val currentAllowance = sharedPref.getFloat("balance", defaultValue).toDouble()

        balance = MutableLiveData(currentAllowance)
    }

    fun add(purchase: Purchase) = viewModelScope.launch {
        repository.insert(purchase)

        val currentAllowance = balance.value?.minus(purchase.cost)

        if (currentAllowance != null) {
            balance.value = currentAllowance

            with(sharedPref.edit()) {
                balance.value?.let { this?.putFloat("balance", it.toFloat()) }
                this?.apply()
            }
        }
    }

    fun getBalanceString(): String {
        return String.format("$%.2f", balance.value)
    }

    fun setBalance(amount: Double) = viewModelScope.launch {

        val currentAllowance: Double =
            balance.value?.plus(
                amount
            ) ?: -1.0

        balance.value = currentAllowance

        with(sharedPref.edit()) {
            balance.value?.let { this?.putFloat("balance", it.toFloat()) }
            this?.apply()
        }

        repository.insert(Purchase(amount, Date(), "-- Increase Balance --"))
    }
}