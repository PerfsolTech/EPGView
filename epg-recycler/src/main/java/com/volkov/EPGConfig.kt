package com.volkov

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.volkov.epg_recycler.R

object EPGConfig {
    /** time header */
    @ColorRes
    var timeHeaderBackground = R.color.time_header_blue

    @ColorRes
    var channelLogoBackground = R.color.time_header_blue

    @DrawableRes
    var channelLogoBackgroundDrawable: Int? = null

    @ColorRes
    var timeHeaderColor = android.R.color.white

    var showDelimiter = true

    /** channel row */
    // height in DP
    var rowHeight = 40

    // margin in dp
    var marginTop = 0
    var marginEnd = 0

    @DrawableRes
    var showBackgroundDrawable = R.drawable.show_background
    var displayFirstShowIcon = false
    var transform: List<BitmapTransformation>? = null
    var logoTransform: List<BitmapTransformation>? = null
    var isProgressVisible = false

    var focusDelay : Long = 0L
}