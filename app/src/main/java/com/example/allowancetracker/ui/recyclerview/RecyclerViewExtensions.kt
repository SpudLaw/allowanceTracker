package com.example.allowancetracker.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View = LayoutInflater.from(context)
    .inflate(layoutRes, this, false)

val RecyclerView.ViewHolder.context: Context
    get() = itemView.context

fun RecyclerView.ViewHolder.string(@StringRes resId: Int, vararg args: Any): String {
    return context.getString(resId, *args)
}

@ColorInt
fun RecyclerView.ViewHolder.color(@ColorRes resId: Int) = getColor(context, resId)
