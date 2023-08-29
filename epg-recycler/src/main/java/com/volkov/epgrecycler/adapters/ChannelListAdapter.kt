package com.volkov.epgrecycler.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.volkov.epg_recycler.R
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.getView
import com.volkov.epgrecycler.viewholders.ChannelViewHolder
import com.volkov.epgrecycler.viewholders.DummyViewHolder

class ChannelListAdapter(
    private val horizontalScrollListener: RecyclerView.OnScrollListener,
) : ListAdapter<DataModel, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CHANNEL -> ChannelViewHolder(getView(parent, R.layout.item_channel))
            VIEW_TYPE_DUMMY -> DummyViewHolder(getView(parent, R.layout.item_dummy))
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChannelViewHolder -> {
                holder.setIsRecyclable(false)
                holder.bind(
                    getItem(position) as DataModel.ChannelDataModel,
                    horizontalScrollListener,
                )
            }
            is DummyViewHolder -> holder.bind(getItem(position) as DataModel.DummyChannel)
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataModel.ChannelDataModel -> VIEW_TYPE_CHANNEL
            is DataModel.DummyChannel -> VIEW_TYPE_DUMMY
            else -> throw IllegalStateException()
        }
    }

    companion object {
        private const val VIEW_TYPE_CHANNEL = 0
        private const val VIEW_TYPE_DUMMY = 1
    }
}