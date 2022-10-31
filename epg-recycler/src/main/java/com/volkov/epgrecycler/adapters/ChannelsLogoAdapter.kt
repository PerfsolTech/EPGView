package com.volkov.epgrecycler.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.volkov.epg_recycler.R
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.getView
import com.volkov.epgrecycler.viewholders.ChannelLogoViewHolder

class ChannelsLogoAdapter : ListAdapter<DataModel, ChannelLogoViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelLogoViewHolder {
        return ChannelLogoViewHolder(getView(parent, R.layout.item_channel_logo))
    }

    override fun onBindViewHolder(holder: ChannelLogoViewHolder, position: Int) {
        holder.bind(getItem(position) as DataModel.ChannelDataModel)
    }
}