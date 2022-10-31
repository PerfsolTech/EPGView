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
        val channelId: Int,
        val channelName: String,
        val logo: String?,
        val shows: List<ShowModel>,
    ) : DataModel("ChannelDataModel_$channelId")

    data class ShowDataModel(
        val channelId: Int,
        val showId: String,
        val name: String,
        val startDate: DateTime,
        val endDate: DateTime,
        val realStartDate: DateTime = startDate,
        val realEndDate: DateTime = endDate,
        val onShowSelect: () -> Unit,
        val onShowClick: () -> Unit,
    ) : DataModel("ShowDataModel_${showId}_${channelId}_${startDate}_${endDate}")

    data class EmptyShow(
        val channelId: Int,
        val isStart: Boolean,
        val width: Int
    ) : DataModel("EmptyShow_${channelId}_${isStart}_$width")
}
