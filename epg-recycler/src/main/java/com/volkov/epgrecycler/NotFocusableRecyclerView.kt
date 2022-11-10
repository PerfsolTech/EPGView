package com.volkov.epgrecycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class NotFocusableRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        descendantFocusability = FOCUS_BLOCK_DESCENDANTS
        focusable = NOT_FOCUSABLE
        isFocusableInTouchMode = false
    }
}