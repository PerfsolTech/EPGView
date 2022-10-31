package com.volkov.epgrecycler.adapters

import androidx.recyclerview.widget.DiffUtil
import com.volkov.epgrecycler.adapters.models.DataModel

class DiffCallback : DiffUtil.ItemCallback<DataModel>() {
    override fun areItemsTheSame(
        oldItem: DataModel,
        newItem: DataModel
    ) = false

    override fun areContentsTheSame(
        oldItem: DataModel,
        newItem: DataModel
    ) = false
}