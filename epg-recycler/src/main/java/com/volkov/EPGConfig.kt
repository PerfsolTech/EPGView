package com.volkov

import android.content.res.ColorStateList
import android.graphics.drawable.ColorStateListDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
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

    @DrawableRes
    var channelDummyBackgroundDrawable: Int? = null

    @ColorRes
    var timeHeaderColor = android.R.color.white

    /** channel row */
    // height in DP
    var rowHeight = 40
    var rowLogoHeight = 40

    // margin in dp
    var marginTop = 0
    var marginVerticalChannelLogo = 0
    var marginHorizontalChannelLogo = 0
    var marginEnd = 0

    @DrawableRes
    var showBackgroundDrawable = R.drawable.show_background
    var showBackgroundColorStateList: ColorStateList? = null
    var displayPreviewForLiveShow = false
    var transform: List<BitmapTransformation>? = null
    var logoTransform: List<BitmapTransformation>? = null
    var isProgressVisible = false
    var showTimeLine = true

    var focusDelay: Long = 0L
}