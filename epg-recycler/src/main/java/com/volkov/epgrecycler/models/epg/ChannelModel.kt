package com.volkov.epgrecycler.models.epg

data class ChannelModel(
    val id: Int,
    val logo: String?,
    val name: String,
    val shows: List<ShowModel>
)
