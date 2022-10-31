package com.volkov.epgrecycler.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.volkov.epg_recycler.R
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.getView
import com.volkov.epgrecycler.viewholders.TimeLineViewHolder

class TimeLineAdapter : ListAdapter<DataModel, TimeLineViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder(getView(parent, R.layout.item_time_line))
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(getItem(position) as DataModel.TimeLineDataModel)
    }
}