package com.volkov.epgrecycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerWithPositionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var scrollListener: OnScrollListener? = null
        set(value) {
            field = value
            value?.let { addOnScrollListener(value) }
        }

    var horizontalPosition: Int = 0
        private set

    var verticalPosition: Int = 0
        private set

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        horizontalPosition += dx
        verticalPosition += dy
    }

    fun scrollHorizontallyBy(dx: Int) {
        clearOnScrollListeners()
        scrollBy(dx, 0)
        scrollListener?.let { addOnScrollListener(it) }
    }

    fun scrollVerticallyBy(dy: Int) {
        clearOnScrollListeners()
        scrollBy(dy, 0)
        scrollListener?.let { addOnScrollListener(it) }
    }

    fun scrollToPos(position: Int) {
        clearOnScrollListeners()
        scrollToPosition(position)
        scrollListener?.let { addOnScrollListener(it) }
    }

    fun scrollHorizontallyTo(dx: Int) {
        val diff = dx - horizontalPosition
        scrollHorizontallyBy(diff)
    }

    override fun focusSearch(direction: Int): View? {
        return null
    }
}