package com.example.allowancetracker.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.recyclerview.widget.RecyclerView
import com.example.allowancetracker.R
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.data.PurchaseType
import com.example.allowancetracker.databinding.ListItemPurchaseBinding


class PurchasesAdapter : RecyclerView.Adapter<PurchasesAdapter.ViewHolder>() {
    var purchaseList: List<Purchase> = emptyList()

    class ViewHolder(private val binding: ListItemPurchaseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceType")
        fun bind(purchase: Purchase) {
            binding.costTextview.text = String.format("$%.2f", purchase.cost)
            binding.descriptionTextview.text = purchase.description

            if (purchase.type != PurchaseType.Purchase) {
                binding.costTextview.setTextColor(Color.parseColor("#008000"))
            } else {
                val ta: TypedArray =
                    binding.costTextview.context.theme.obtainStyledAttributes(R.styleable.ViewStyle)

                val labelColor: Int = ta.getColor(R.styleable.ViewStyle_labelColor, R.color.black)

                binding.costTextview.setTextColor(labelColor)
            }
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
        purchaseList = purchases.asReversed()
        notifyDataSetChanged()
    }
}