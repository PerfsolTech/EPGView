package com.volkov.epgrecycler.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.epg_recycler.databinding.ItemShowEmptyBinding
import com.volkov.epgrecycler.adapters.models.DataModel

class EmptyShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemShowEmptyBinding::bind)

    @SuppressLint("SetTextI18n")
    fun bind(item: DataModel.EmptyShow) {
        binding.llEmpty.updateLayoutParams<RecyclerView.LayoutParams> {
            width = item.width
        }
    }
}