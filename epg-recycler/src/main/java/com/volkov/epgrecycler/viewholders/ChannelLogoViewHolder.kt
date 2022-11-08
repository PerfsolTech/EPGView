package com.volkov.epgrecycler.viewholders

import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.volkov.EPGConfig
import com.volkov.epg_recycler.databinding.ItemChannelLogoBinding
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.context
import com.volkov.epgrecycler.dpToPx

class ChannelLogoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemChannelLogoBinding::bind)

    fun bind(item: DataModel.ChannelDataModel) {
        val options = RequestOptions().apply {
            EPGConfig.logoTransform?.let { list ->
                list.map {
                    transform(it)
                }
            }
        }
        Glide
            .with(binding.root)
            .load(item.logo)
            .apply(options)
            .into(binding.root)

        binding.root.background = EPGConfig.channelLogoBackgroundDrawable?.let { context.getDrawable(it) }

        binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
            height = EPGConfig.rowHeight.dpToPx
            topMargin = EPGConfig.marginTop.dpToPx
        }
    }
}