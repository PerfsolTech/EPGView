package com.volkov.epgrecycler.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.epg_recycler.databinding.ItemChannelBinding
import com.volkov.epgrecycler.EPGRecyclerView
import com.volkov.epgrecycler.EPGUtils
import com.volkov.epgrecycler.EPGUtils.startTime
import com.volkov.epgrecycler.RecyclerWithPositionView
import com.volkov.epgrecycler.adapters.ShowAdapter
import com.volkov.epgrecycler.adapters.models.DataModel
import org.joda.time.DateTime

class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding by viewBinding(ItemChannelBinding::bind)
    private val epgAdapter = ShowAdapter()
    private lateinit var startDate: DateTime
    private lateinit var endDate: DateTime

    private val horizontalRecyclerView: RecyclerWithPositionView
        get() = binding.root

    fun bind(
        item: DataModel.ChannelDataModel,
        horizontalScrollListener: RecyclerView.OnScrollListener,
        eventListener: EPGRecyclerView.OnEventListener? = null,
    ) {
        binding.root.tag = "channel_${item.channelId}"
        startDate = item.shows.firstOrNull()?.startDate ?: return
        endDate = DateTime(item.shows.last().endDate)
        horizontalRecyclerView.apply {
            val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = epgAdapter
            layoutManager = lm
            setHasFixedSize(false)
            this.scrollListener = horizontalScrollListener
        }
        val showDataModelList: MutableList<DataModel> = item.shows.map { show ->
            DataModel.ShowDataModel(
                channelId = item.channelId,
                showId = show.id,
                name = show.name,
                startDate = show.startDate,
                endDate = show.endDate,
                realStartDate = show.realStartDate,
                realEndDate = show.realEndDate,
                onShowSelect = {
                    eventListener?.onShowSelected(item.channelId, show.id)
                },
                onShowClick = {
                    eventListener?.onShowClick(item.channelId, show.id)
                },
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