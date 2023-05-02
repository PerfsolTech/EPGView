package com.volkov.epgrecycler.models.epg

import org.joda.time.DateTime

data class ShowModel(
    val id: String,
    val showPreviewImage: String?,
    val channelId: String,
    val name: String,
    val startDate: DateTime,
    val endDate: DateTime,
)
