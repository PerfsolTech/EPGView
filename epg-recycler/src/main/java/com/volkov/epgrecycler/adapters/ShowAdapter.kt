package com.volkov.epgrecycler.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.volkov.epg_recycler.R
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.getView
import com.volkov.epgrecycler.viewholders.EmptyShowViewHolder
import com.volkov.epgrecycler.viewholders.ShowViewHolder

class ShowAdapter : ListAdapter<DataModel, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SHOW -> ShowViewHolder(getView(parent, R.layout.item_show))
            else -> EmptyShowViewHolder(getView(parent, R.layout.item_show_empty))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        when (holder) {
            is ShowViewHolder -> holder.bind(getItem(position) as DataModel.ShowDataModel)
            is EmptyShowViewHolder -> holder.bind(getItem(position) as DataModel.EmptyShow)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataModel.EmptyShow -> VIEW_TYPE_SHOW_EMPTY
            else -> VIEW_TYPE_SHOW
        }
    }

    companion object {
        private const val VIEW_TYPE_SHOW = 0
        private const val VIEW_TYPE_SHOW_EMPTY = 1
    }
}