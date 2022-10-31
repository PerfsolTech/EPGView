package com.volkov.epgrecycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class RecyclerWithPositionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var scrollListener: OnScrollListener? = null
        set(value) {
            field = value
            value?.let { addOnScrollListener(value) }
        }

    private var horizontalPosition: Int = 0

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        horizontalPosition += dx
    }

    fun scrollHorizontallyBy(dx: Int) {
        clearOnScrollListeners()
        scrollBy(dx, 0)
        scrollListener?.let { addOnScrollListener(it) }
    }

    fun scrollHorizontallyTo(dx: Int) {
        val diff = dx - horizontalPosition
        scrollHorizontallyBy(diff)
    }
}