package com.example.allowancetracker.ui.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.data.PurchaseDatabase
import com.example.allowancetracker.data.PurchaseRepository
import com.example.allowancetracker.data.PurchaseType
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PurchaseRepository
    val balance: LiveData<Double>
    val balanceString: LiveData<String>
    var purchases: LiveData<List<Purchase>>

    init {
        val purchaseDao = PurchaseDatabase.getDatabase(application, viewModelScope).purchaseDao()
        repository = PurchaseRepository(purchaseDao)
        purchases = repository.allPurchases

        balance = purchases.map { //Each time we get a new list of purchases

            //Starting with a 0.0 balance, loop through the list
            it.fold(0.0){ total, purchase ->
                when(purchase.type){
                    //If a purchase is a deposit, we add it to the total
                    PurchaseType.InitialDeposit,
                    PurchaseType.Deposit ->  total + purchase.cost
                    //If a purchase is a purchase, we remove it from the total
                    PurchaseType.Purchase ->  total - purchase.cost
                }
            }
        }

        balanceString = balance.map { String.format("$%.2f", it) }
    }

    fun add(purchase: Purchase) = viewModelScope.launch {
        repository.insert(purchase)
    }
}