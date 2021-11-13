package com.example.allowancetracker.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.databinding.ListItemPurchaseBinding

class PurchasesAdapter : RecyclerView.Adapter<PurchasesAdapter.ViewHolder>() {
    var purchaseList: List<Purchase> = emptyList()

    class ViewHolder(private val binding: ListItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(purchase: Purchase) {
            binding.costTextview.text = String.format("%.2f", purchase.cost)
            binding.descriptionTextview.text = purchase.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPurchaseBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(purchaseList[position])
    }

    override fun getItemCount() = purchaseList.count()

    fun setPurchases(purchases: List<Purchase>) {
        purchaseList = purchases
        notifyDataSetChanged()
    }
}