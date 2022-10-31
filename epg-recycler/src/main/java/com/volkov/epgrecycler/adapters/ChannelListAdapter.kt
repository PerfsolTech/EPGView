package com.volkov.epgrecycler.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.volkov.epg_recycler.R
import com.volkov.epgrecycler.EPGRecyclerView
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.getView
import com.volkov.epgrecycler.viewholders.ChannelViewHolder

class ChannelListAdapter(
    private val horizontalScrollListener: RecyclerView.OnScrollListener,
    private val listener: EPGRecyclerView.OnEventListener? = null,
) : ListAdapter<DataModel, ChannelViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        return ChannelViewHolder(getView(parent, R.layout.item_channel))
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(
            getItem(position) as DataModel.ChannelDataModel,
            horizontalScrollListener,
            listener,
        )
    }
}