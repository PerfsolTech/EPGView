package com.volkov.epgrecycler.viewholders

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.volkov.EPGConfig
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
import org.joda.time.Seconds

class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemShowBinding::bind)

    @SuppressLint("SetTextI18n")
    fun bind(item: DataModel.ShowDataModel) {
        binding.background.tag = "${item.channelId}#${item.showId}"
        binding.background.background =
            ContextCompat.getDrawable(context, EPGConfig.showBackgroundDrawable)

        EPGConfig.showBackgroundColorStateList?.let {
            binding.background.backgroundTintList = it
        }

        binding.background.isActivated = item.isLiveShow

        binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
            marginEnd = EPGConfig.marginEnd.dpToPx
        }

        binding.ivShowImage.isVisible = EPGConfig.displayPreviewForLiveShow && item.isLiveShow

        val progressMax = Seconds.secondsBetween(item.startDate, item.endDate).seconds
        val progress = Seconds.secondsBetween(item.startDate, DateTime.now()).seconds
        binding.pbLine.isVisible = EPGConfig.isProgressVisible && binding.ivShowImage.isVisible
        binding.pbLine.max = progressMax
        binding.pbLine.progress = progress

        if (binding.ivShowImage.isVisible) {
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

        binding.tvTitle.text = item.name
        binding.tvSubTitle.text =
            "${realStartDate.toString(SHOW_TIME_PATTERN)} - ${realEndDate.toString(SHOW_TIME_PATTERN)}"

        binding.showParent.updateLayoutParams<RecyclerView.LayoutParams> {
            width = getCellWidth(start, end)
        }

        binding.root.setOnClickListenerDebounce {
            item.onClick?.invoke()
        }
    }

    private val DataModel.ShowDataModel.isLiveShow: Boolean
        get() = startDate.isBeforeNow && endDate.isAfterNow
}