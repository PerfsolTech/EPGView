package com.volkov.epgrecycler

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

val View?.isViewOnScreen: Boolean
    get() {
        return this?.let {
            if (!isShown) {
                return false
            }
            val actualPosition = Rect()
            val isGlobalVisible = getGlobalVisibleRect(actualPosition)
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels
            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            val screen = Rect(0, 0, screenWidth, screenHeight)
            return isGlobalVisible && Rect.intersects(actualPosition, screen)
        } ?: false
    }

val ViewHolder.context: Context
    get() = itemView.context