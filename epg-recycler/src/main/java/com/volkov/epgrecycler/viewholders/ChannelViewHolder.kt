package com.volkov.epgrecycler.viewholders

import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.EPGConfig
import com.volkov.epg_recycler.databinding.ItemChannelBinding
import com.volkov.epgrecycler.EPGUtils
import com.volkov.epgrecycler.EPGUtils.startTime
import com.volkov.epgrecycler.RecyclerWithPositionView
import com.volkov.epgrecycler.adapters.ShowAdapter
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.dpToPx

class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemChannelBinding::bind)
    private val epgAdapter = ShowAdapter()

    private val horizontalRecyclerView: RecyclerWithPositionView
        get() = binding.root

    fun bind(
        item: DataModel.ChannelDataModel,
        horizontalScrollListener: RecyclerView.OnScrollListener
    ) {
        binding.root.tag = "channel_${item.channelId}"

        binding.root.updateLayoutParams<RecyclerView.LayoutParams> {
            height = EPGConfig.rowHeight.dpToPx
            topMargin = EPGConfig.marginTop.dpToPx
        }

        item.shows.firstOrNull()?.startDate ?: return
        horizontalRecyclerView.apply {
            val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = epgAdapter
            layoutManager = lm
            setHasFixedSize(false)
            this.scrollListener = horizontalScrollListener
        }
        val showDataModelList: MutableList<DataModel> = item.shows.mapIndexed { index, show ->
            DataModel.ShowDataModel(
                showIndex = index,
                channelId = item.channelId,
                showId = show.id,
                name = show.name,
                showPreviewImage = show.showPreviewImage,
                startDate = show.startDate,
                endDate = show.endDate,
                onClick = {
                    item.onShowClick?.invoke(show.id)
                }
            )
        }.toMutableList()
        item.shows.first().let { show ->
            val startDate = show.startDate
            if (startDate.isAfter(startTime)) {
                showDataModelList.add(
                    index = 0,
                    element = DataModel.EmptyShow(
                        channelId = item.channelId,
                        isStart = true,
                        width = EPGUtils.getCellWidth(startTime, startDate)
                    )
                )
            }
        }

        item.shows.last().let { show ->
            val endDate = show.endDate
            if (endDate.isBefore(EPGUtils.endTime)) {
                showDataModelList.add(
                    element = DataModel.EmptyShow(
                        channelId = item.channelId,
                        isStart = false,
                        width = EPGUtils.getCellWidth(endDate, EPGUtils.endTime)
                    )
                )
            }
        }
        epgAdapter.submitList(showDataModelList) {
            horizontalRecyclerView.apply {
                post {
                    scrollHorizontallyTo(EPGUtils.getCellWidth(startTime, EPGUtils.currentEpgTime))
                }
            }
        }
    }
}