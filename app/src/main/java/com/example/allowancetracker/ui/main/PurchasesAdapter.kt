package com.example.allowancetracker.ui.main

import android.content.res.TypedArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.allowancetracker.R
import com.example.allowancetracker.data.Purchase
import com.example.allowancetracker.databinding.ListItemPurchaseBinding
import com.example.allowancetracker.ui.recyclerview.color
import com.example.allowancetracker.ui.recyclerview.context
import com.example.allowancetracker.ui.recyclerview.inflate
import com.example.allowancetracker.ui.recyclerview.string


private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Purchase>() {
    override fun areItemsTheSame(oldItem: Purchase, newItem: Purchase): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Purchase, newItem: Purchase): Boolean {
        return oldItem == newItem
    }
}

class PurchasesAdapter : ListAdapter<Purchase, PurchaseHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseHolder {
        return PurchaseHolder(parent.inflate(R.layout.list_item_purchase))
    }

    override fun onBindViewHolder(holder: PurchaseHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PurchaseHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(purchase: Purchase): Unit = with(ListItemPurchaseBinding.bind(itemView)) {
        costTextview.text = string(R.string.cost, purchase.cost)
        descriptionTextview.text = purchase.description

        if (purchase.isIncrease) {
            costTextview.setTextColor(color(R.color.cost_increase))
        } else {
            val ta: TypedArray = context.theme.obtainStyledAttributes(R.styleable.ViewStyle)
            val labelColor = ta.getColor(R.styleable.ViewStyle_labelColor, color(R.color.black))

            costTextview.setTextColor(labelColor)
        }
    }
}
