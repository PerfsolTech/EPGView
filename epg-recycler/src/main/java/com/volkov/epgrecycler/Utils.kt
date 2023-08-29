package com.volkov.epgrecycler

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder

val Number.dpToPx
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

// inflate viewHolder
fun getView(parent: ViewGroup, layout: Int): View {
    return LayoutInflater.from(parent.context).inflate(layout, parent, false)
}

val ViewHolder.context: Context
    get() = itemView.context

fun Context.getColorRes(@ColorRes color: Int) = ContextCompat.getColor(this, color)