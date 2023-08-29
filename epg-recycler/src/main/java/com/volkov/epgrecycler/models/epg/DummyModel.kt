package com.volkov.epgrecycler.models.epg

data class DummyModel(
    val title: String,
    val onClick: (() -> Unit)? = null
)
