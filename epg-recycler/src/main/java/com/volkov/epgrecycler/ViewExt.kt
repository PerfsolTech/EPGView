package com.volkov.epgrecycler

import android.os.SystemClock
import android.view.View

fun View.setOnClickListenerDebounce(debounceTime: Long = 300L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}