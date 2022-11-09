package com.volkov.epgrecycler.models.epg

data class ChannelModel(
    val id: String,
    val logo: String?,
    val name: String,
    val shows: List<ShowModel>
)
