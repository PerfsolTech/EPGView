package com.volkov.epgrecycler.viewholders

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.EPGConfig
import com.volkov.epg_recycler.R
import com.volkov.epg_recycler.databinding.ItemDummyBinding
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.context
import com.volkov.epgrecycler.setOnClickListenerDebounce

class DummyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemDummyBinding::bind)

    fun bind(item: DataModel.DummyChannel) {
        binding.root.background =
            EPGConfig.channelDummyBackgroundDrawable?.let {
                AppCompatResources.getDrawable(context, it)
            }
        binding.root.tag =
            context.getString(R.string.dummy_channel_item, "${item.dummyIndex}_${item.title}")
        binding.root.setOnClickListenerDebounce { item.onClick?.invoke() }
        binding.tvTitle.text = item.title
    }
}