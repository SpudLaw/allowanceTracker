package com.example.allowancetracker.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class PurchaseRepository(private val purchaseDao: PurchaseDao) {
    val allPurchases: LiveData<List<Purchase>> = purchaseDao.getAll()

    @WorkerThread
    suspend fun insert(purchase: Purchase) {
        purchaseDao.add(purchase)
    }

    @WorkerThread
    suspend fun delete(purchase: Purchase) {
        purchaseDao.delete(purchase.id)
    }
}