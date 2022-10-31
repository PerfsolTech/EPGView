package com.volkov.epgrecycler.viewholders

import android.view.View
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.epg_recycler.databinding.ItemTimeLineBinding
import com.volkov.epgrecycler.adapters.models.DataModel

class TimeLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemTimeLineBinding::bind)

    fun bind(item: DataModel.TimeLineDataModel) {
        binding.llTimeLineParent.updateLayoutParams<RecyclerView.LayoutParams> {
            marginStart = item.marginStart
        }
        binding.tvTime.text = item.time
        binding.line.updateLayoutParams<LinearLayout.LayoutParams> {
            gravity = item.gravity
        }
    }
}