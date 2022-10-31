package com.volkov.epgrecycler.models.epg

import org.joda.time.DateTime

data class ShowModel(
    val id: String,
    val channelId: Int,
    val name: String,
    val startDate: DateTime,
    val endDate: DateTime,
    val realStartDate: DateTime = startDate,
    val realEndDate: DateTime = endDate,
    val isDuplicate: Boolean = false,
) {
    val showTag = "$channelId#$id"
}
