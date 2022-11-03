package com.volkov.epgrecycler.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.volkov.EPGConfig
import com.volkov.EPGConfig.marginStart
import com.volkov.epg_recycler.databinding.ItemShowBinding
import com.volkov.epgrecycler.EPGUtils.SHOW_TIME_PATTERN
import com.volkov.epgrecycler.EPGUtils.endTime
import com.volkov.epgrecycler.EPGUtils.getCellWidth
import com.volkov.epgrecycler.EPGUtils.startTime
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.context
import com.volkov.epgrecycler.dpToPx
import com.volkov.epgrecycler.setOnClickListenerDebounce
import org.joda.time.DateTime

class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemShowBinding::bind)

    @SuppressLint("SetTextI18n")
    fun bind(item: DataModel.ShowDataModel) {
        binding.showParent.tag = "${item.channelId}#${item.showId}"
        binding.root.background =
            ContextCompat.getDrawable(context, EPGConfig.showBackgroundDrawable)

        binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
            marginStart = EPGConfig.marginStart.dpToPx
        }

        binding.ivShowImage.isVisible = EPGConfig.displayFirstShowIcon && item.showIndex == 0
        binding.llShowName.updateLayoutParams<ConstraintLayout.LayoutParams> {
            marginStart = if (EPGConfig.displayFirstShowIcon) 10.dpToPx else 0
        }
        if (EPGConfig.displayFirstShowIcon) {
            val options = RequestOptions().apply {
                EPGConfig.transform?.let { list ->
                    list.map {
                        transform(it)
                    }
                }
            }
            Glide.with(context)
                .load(item.showPreviewImage)
                .apply(options)
                .into(binding.ivShowImage)
        }

        val realStartDate = DateTime(item.startDate)
        val realEndDate = DateTime(item.endDate)
        val start = if (item.startDate.isBefore(startTime)) startTime else item.startDate
        val end = if (item.endDate.isAfter(endTime)) endTime else item.endDate

        val isActivated = realStartDate.isBeforeNow && realEndDate.isAfterNow
        binding.showParent.isActivated = isActivated

        binding.tvTitle.text = item.name
        binding.tvSubTitle.text =
            "${realStartDate.toString(SHOW_TIME_PATTERN)} - ${realEndDate.toString(SHOW_TIME_PATTERN)}"

        binding.showParent.updateLayoutParams<RecyclerView.LayoutParams> {
            width = getCellWidth(start, end)
        }

        binding.showParent.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                item.onShowSelect.invoke()
            }
        }

        binding.showParent.setOnClickListenerDebounce {
            item.onShowClick.invoke()
        }
    }
}