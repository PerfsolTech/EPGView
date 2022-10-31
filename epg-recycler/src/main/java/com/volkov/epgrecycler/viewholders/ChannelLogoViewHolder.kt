package com.volkov.epgrecycler.viewholders

import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.volkov.EPGConfig
import com.volkov.epg_recycler.databinding.ItemChannelLogoBinding
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.dpToPx

class ChannelLogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemChannelLogoBinding::bind)

    fun bind(item: DataModel.ChannelDataModel) {
        Glide
            .with(binding.root)
            .load(item.logo)
            .into(binding.root)

        binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
            height = EPGConfig.rowHeight.dpToPx
            topMargin = EPGConfig.marginTop.dpToPx
        }
    }
}