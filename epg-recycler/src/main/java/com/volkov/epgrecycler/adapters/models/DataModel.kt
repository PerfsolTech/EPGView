package com.volkov.epgrecycler.adapters.models

import android.view.Gravity
import com.volkov.epgrecycler.models.epg.ShowModel
import org.joda.time.DateTime

sealed class DataModel(val id: String) {

    data class TimeLineDataModel(
        val timeId: String,
        val time: String,
        val gravity: Int = Gravity.CENTER,
        val marginStart: Int,
    ) : DataModel("TimeLineDataModel_$timeId")

    data class ChannelDataModel(
        val channelId: String,
        val channelName: String,
        val logo: String?,
        val shows: List<ShowModel>,
        val onShowClick: ((String) -> Unit)? = null
    ) : DataModel("ChannelDataModel_$channelId")

    data class ShowDataModel(
        val showIndex: Int,
        val channelId: String,
        val showId: String,
        val name: String,
        val showPreviewImage: String?,
        val startDate: DateTime,
        val endDate: DateTime,
        val onClick: (() -> Unit)? = null
    ) : DataModel("ShowDataModel_${showId}_${channelId}_${startDate}_${endDate}")

    data class EmptyShow(
        val channelId: String,
        val isStart: Boolean,
        val width: Int
    ) : DataModel("EmptyShow_${channelId}_${isStart}_$width")

    data class DummyChannel(
        val title: String,
        val dummyIndex: Int,
        val onClick: (() -> Unit)? = null,
    ) : DataModel("DummyChannel_$title")
}
