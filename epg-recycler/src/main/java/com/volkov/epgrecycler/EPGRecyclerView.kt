package com.volkov.epgrecycler

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.EPGConfig
import com.volkov.epg_recycler.R
import com.volkov.epg_recycler.databinding.ViewEpgRecyclerBinding
import com.volkov.epgrecycler.Constants.TIME_HEADER
import com.volkov.epgrecycler.EPGUtils.currentEpgTime
import com.volkov.epgrecycler.EPGUtils.dayShift
import com.volkov.epgrecycler.EPGUtils.getCellWidth
import com.volkov.epgrecycler.EPGUtils.getCellWidthSeconds
import com.volkov.epgrecycler.EPGUtils.getDayLength
import com.volkov.epgrecycler.EPGUtils.maxHour
import com.volkov.epgrecycler.EPGUtils.minuteToPixel
import com.volkov.epgrecycler.EPGUtils.startTime
import com.volkov.epgrecycler.EPGUtils.timeLabelWidth
import com.volkov.epgrecycler.adapters.ChannelListAdapter
import com.volkov.epgrecycler.adapters.ChannelsLogoAdapter
import com.volkov.epgrecycler.adapters.TimeLineAdapter
import com.volkov.epgrecycler.adapters.models.DataModel
import com.volkov.epgrecycler.models.epg.ChannelModel
import com.volkov.epgrecycler.models.epg.DummyModel
import com.volkov.epgrecycler.models.epg.ShowModel
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Minutes
import kotlin.math.abs
import kotlin.math.max

class EPGRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding by viewBinding(ViewEpgRecyclerBinding::bind)

    private var channels: List<ChannelModel> = emptyList()
    private var dummyItems: List<DummyModel> = emptyList()
    private var currentDummyIndex = -1

    private var lastSelectedShowView: View? = null
    private var lastSelectedShowViewTemp: View? = null
    private var lastSelectedDummyView: View? = null

    interface OnEventListener {
        fun onShowSelected(channelId: String, showId: String)
        fun onShowClick(channelId: String, showId: String)
        fun onShowExit()
    }

    var listener: OnEventListener? = null

    enum class MoveDirection {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    private val horizontalScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            scrollAll(recyclerView, dx)
            scrollTimeHeader(recyclerView, dx)
            updateTimeIndicator(withSubmit = false)
        }
    }

    @Suppress("unused")
    fun setTimeZone(zone: DateTimeZone) {
        EPGUtils.timeZone = zone
    }

    fun setStartHour(startHour: Int) {
        EPGUtils.startHour = startHour
    }

    fun setEndHour(endHour: Int) {
        EPGUtils.endHour = endHour
    }

    fun setDayShift(day: Int) {
        if (day < 0 || day > 6) return
        dayShift = day
    }

    private val verticalScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            scrollChannels(recyclerView, dy)
            scrollChannelsLogo(recyclerView, dy)
        }
    }

    private val timeAdapter = TimeLineAdapter()
    private val channelsLogoAdapter = ChannelsLogoAdapter()
    private var timeLineScrollPosition = 0

    private val focusListener = OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) lastSelectedShowView?.isSelected = true
    }

    init {
        inflate(context, R.layout.view_epg_recycler, this)
        binding.rvTimeLine.apply {
            tag = TIME_HEADER
            layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
            adapter = timeAdapter
            setHasFixedSize(true)
            itemAnimator = null
            addOnScrollListener(horizontalScrollListener)
            addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return true
                }
            })
        }

        binding.rvChannelsLogos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = channelsLogoAdapter
            setHasFixedSize(true)
            itemAnimator = null
            addOnScrollListener(verticalScrollListener)
            onFocusChangeListener = focusListener
        }
    }

    fun initView(
        channels: List<ChannelModel>,
        dummyItems: List<DummyModel> = emptyList(),
        initChannel: String? = null
    ) {
        initUI()
        setTimeHeader()
        initChannelRecycler()
        this.channels = channels
        this.dummyItems = dummyItems
        val mappedChannels = this.channels.mapChannels()
        val mappedDummies = this.dummyItems.mapDummyItems()
        setChannelsLogo(mappedChannels)
        setChannels(mappedChannels + mappedDummies)
        scrollToNow()
        post {
            selectCurrentShow(initChannel ?: channels.firstOrNull()?.id)
            binding.rvChannels.requestFocus()
        }
    }

    @SuppressLint("NewApi")
    private fun initUI() {
        binding.rvTimeLine.setBackgroundColor(context.getColorRes(EPGConfig.timeHeaderBackground))
        binding.rvChannelsLogos.setBackgroundColor(context.getColorRes(EPGConfig.channelLogoBackground))
    }

    private fun getNextShowByDirection(direction: MoveDirection): View? {
        val currentChannelTag = lastSelectedShowView?.tag?.toString()?.split("#") ?: return null
        val channelId = currentChannelTag.firstOrNull() ?: return null
        val showId = currentChannelTag.getOrNull(1) ?: return null
        val currentChannel = channels.firstOrNull { it.id == channelId } ?: return null
        val currentChannelIndex = channels.indexOf(currentChannel)
        val (channel, desiredShow) = when (direction) {
            MoveDirection.UP -> {
                val prevChannel = channels.getOrNull(currentChannelIndex - 1) ?: return null
                val currentChannelShow =
                    currentChannel.shows.firstOrNull { it.id == showId } ?: return null
                val foundingTime = currentChannelShow.startDate.plusSeconds(1)
                val desiredShow = prevChannel.shows.singleOrNull {
                    it.startDate.isBefore(foundingTime) && it.endDate.isAfter(foundingTime)
                } ?: prevChannel.shows.firstOrNull {
                    it.startDate.isAfter(foundingTime)
                } ?: prevChannel.shows.lastOrNull {
                    it.endDate.isBefore(foundingTime)
                } ?: return null
                Pair(prevChannel, desiredShow)
            }

            MoveDirection.DOWN -> {
                val nextChannel = channels.getOrNull(currentChannelIndex + 1) ?: return null
                val currentChannelShow =
                    currentChannel.shows.firstOrNull { it.id == showId } ?: return null
                val foundingTime = currentChannelShow.startDate.plusSeconds(1)
                val desiredShow = nextChannel.shows.firstOrNull {
                    it.startDate.isBefore(foundingTime) && it.endDate.isAfter(foundingTime)
                } ?: nextChannel.shows.firstOrNull {
                    it.startDate.isAfter(foundingTime)
                } ?: nextChannel.shows.lastOrNull {
                    it.endDate.isBefore(foundingTime)
                } ?: return null
                Pair(nextChannel, desiredShow)
            }

            MoveDirection.LEFT -> {
                val currentChannelShow =
                    currentChannel.shows.singleOrNull { it.id == showId } ?: return null
                val currentShowIndex = currentChannel.shows.indexOf(currentChannelShow)
                val desiredShow =
                    currentChannel.shows.getOrNull(currentShowIndex - 1) ?: return null
                Pair(currentChannel, desiredShow)
            }

            MoveDirection.RIGHT -> {
                val currentChannelShow =
                    currentChannel.shows.singleOrNull { it.id == showId } ?: return null
                val currentShowIndex = currentChannel.shows.indexOf(currentChannelShow)
                val desiredShow =
                    currentChannel.shows.getOrNull(currentShowIndex + 1) ?: return null
                Pair(currentChannel, desiredShow)
            }

            MoveDirection.NONE -> return null
        }
        selectShow(channel, desiredShow)
        return lastSelectedShowView
    }

    private fun selectDummyChannel(direction: MoveDirection): View? {
        lastSelectedDummyView?.isSelected = false
        if (direction == MoveDirection.DOWN) {
            if (currentDummyIndex == dummyItems.lastIndex) currentDummyIndex = dummyItems.lastIndex
            else currentDummyIndex++
        } else if (direction == MoveDirection.UP) {
            if (currentDummyIndex <= 0) currentDummyIndex = -1 else currentDummyIndex--
        } else return null
        val dummyItem = dummyItems.getOrNull(currentDummyIndex) ?: return null
        val dummyIndex = dummyItems.indexOf(dummyItem)
        val viewTag =
            context.getString(R.string.dummy_channel_item, "${dummyIndex}_${dummyItem.title}")
        val view = binding.rvChannels.findViewWithTag<View>(viewTag) ?: return null
        view.isSelected = true
        return view
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return when {
            event.onDownPressed() -> {
                if (getNextShowByDirection(MoveDirection.DOWN) == null) {
                    lastSelectedDummyView = selectDummyChannel(MoveDirection.DOWN)
                    if (lastSelectedDummyView != null) {
                        lastSelectedShowView?.isSelected = false
                        lastSelectedShowView?.let { lastSelectedShowViewTemp = it }
                        lastSelectedShowView = null
                    }
                }
                true
            }

            event.onUpPressed() -> {
                lastSelectedDummyView = selectDummyChannel(MoveDirection.UP)
                if (lastSelectedDummyView == null) {
                    lastSelectedShowViewTemp?.let {
                        lastSelectedShowView = it
                        lastSelectedShowView?.isSelected = true
                        lastSelectedShowViewTemp = null
                        return true
                    }

                    if (getNextShowByDirection(MoveDirection.UP) == null) {
                        lastSelectedShowView?.isSelected = false
                        listener?.onShowExit()
                    }
                }
                true
            }

            event.onLeftPressed() -> {
                getNextShowByDirection(MoveDirection.LEFT)
                true
            }

            event.onRightPressed() -> {
                getNextShowByDirection(MoveDirection.RIGHT)
                true
            }

            event.onEnterPressed() -> {
                lastSelectedDummyView?.let {
                    it.performClick()
                    return true
                }
                lastSelectedShowView?.let {
                    val tag = lastSelectedShowView?.tag.toString().split("#")
                    listener?.onShowClick(tag[0], tag[1])
                }
                true
            }

            else -> super.dispatchKeyEvent(event)
        }
    }

    private fun initChannelRecycler() {
        binding.rvChannels.apply {
            val channelListAdapter = ChannelListAdapter(horizontalScrollListener)
            layoutManager = object : LinearLayoutManager(context) {
                override fun onInterceptFocusSearch(focused: View, direction: Int): View? = null
            }
            adapter = channelListAdapter
            setHasFixedSize(true)
            addOnScrollListener(verticalScrollListener)
            onFocusChangeListener = focusListener
        }
    }

    fun updateTimeIndicator(withSubmit: Boolean = true) {
        val now = DateTime()
        val indicatorPosition = getCellWidthSeconds(startTime, now)
        val isVisible = indicatorPosition - timeLineScrollPosition in (0..binding.rvTimeLine.width)
        binding.timeIndicator.isVisible = isVisible && EPGConfig.showTimeLine
        binding.tvTimeLineLabel.isVisible = binding.timeIndicator.isVisible
        binding.timeIndicator.updateLayoutParams<LayoutParams> {
            marginStart = getCellWidthSeconds(currentEpgTime, now)
        }
        binding.tvTimeLineLabel.updateLayoutParams<LayoutParams> {
            marginStart = binding.timeIndicator.marginStart - 25.dpToPx
        }
        binding.tvTimeLineLabel.text = DateTime().toString("HH:mm")
        if (!withSubmit) return
        binding.rvChannels.children.toList().map { it as? RecyclerView }.forEach {
            it?.children?.toList()?.map { view -> view.tag?.toString() ?: "" }?.forEach { tag ->
                if (tag.isNotEmpty() && tag.contains("#")) {
                    val v = tag.split("#")
                    val channelId = v.firstOrNull()
                    channels.singleOrNull { channel -> channel.id == channelId }?.let { channel ->
                        val currentShow = channel.shows.getCurrentShow()
                        findViewWithTag<View>(tag).isActivated = currentShow?.id == v[1]
                    }
                }
            }
        }
    }

    private fun selectCurrentShow(channelId: String?) {
        val channel =
            channels.singleOrNull { it.id == channelId } ?: channels.firstOrNull() ?: return
        val channelIndex = channels.indexOf(channel)
        scrollVerticallyToPosition(channelIndex)
        val currentShow =
            if (dayShift == 0) channel.shows.getCurrentShow() ?: channel.shows.firstOrNull()
            else channel.shows.firstOrNull()
        currentShow?.let {
            selectShow(channel, currentShow)
        }
    }

    private fun scrollVerticallyToPosition(position: Int) {
        val rowHeightPx = EPGConfig.rowHeight.dpToPx
        val marginTopPx = EPGConfig.marginTop.dpToPx
        val scrollToBot = marginTopPx * (position + 1) + rowHeightPx * (position + 1)
        val scrollToTop = scrollToBot - rowHeightPx
        val recycler = binding.rvChannelsLogos
        val recyclerRange = recycler.verticalPosition..recycler.verticalPosition + recycler.height
        if (scrollToTop in recyclerRange && scrollToBot in recyclerRange) return
        val topScroll = abs(scrollToTop - recyclerRange.first)
        val botScroll = abs(scrollToBot - recyclerRange.last)
        if (topScroll < botScroll) {
            scrollChannelsLogo(null, -topScroll)
            scrollChannels(null, -topScroll)
        } else {
            scrollChannelsLogo(null, botScroll)
            scrollChannels(null, botScroll)
        }
    }

    @Suppress("unused")
    fun selectShow(
        channelId: String, showId: String
    ) {
        val channel = channels.singleOrNull { it.id == channelId } ?: return
        val show = channel.shows.singleOrNull { it.id == showId } ?: return
        selectShow(channel, show)
    }

    private fun selectShow(
        channel: ChannelModel, show: ShowModel
    ) {
        scrollVerticallyToPosition(channels.indexOf(channel))
        val tag = "${channel.id}#${show.id}"
        postDelayed({
            val channelRecycler =
                binding.rvChannels.findViewWithTag<RecyclerWithPositionView>("channel_${channel.id}")
                    ?: return@postDelayed

            val showIndex = channel.shows.indexOf(show)

            val start = if (show.startDate.isBefore(startTime)) startTime else show.startDate
            val end = if (show.endDate.isAfter(EPGUtils.endTime)) EPGUtils.endTime else show.endDate
            val showWidth = Minutes.minutesBetween(start, end).minutes * minuteToPixel
            val scrollTo = Minutes.minutesBetween(
                startTime, start
            ).minutes * minuteToPixel + showIndex * EPGConfig.marginEnd.dpToPx

            val scrollToEnd = Minutes.minutesBetween(
                startTime, end
            ).minutes * minuteToPixel + showIndex * EPGConfig.marginEnd.dpToPx
            val recyclerRange =
                channelRecycler.horizontalPosition..channelRecycler.horizontalPosition + channelRecycler.width
            apply {
                if (scrollTo in recyclerRange && scrollToEnd in recyclerRange) return@apply
                // if show width less then screen width, trying to fit show to screen by sides
                if (showWidth <= channelRecycler.width) {
                    val leftScroll = abs(scrollTo - recyclerRange.first)
                    val rightScroll = abs(scrollToEnd - recyclerRange.last)
                    if (leftScroll < rightScroll) {
                        scrollTimeHeader(null, -leftScroll)
                        scrollAll(null, -leftScroll)
                    } else {
                        scrollTimeHeader(null, rightScroll)
                        scrollAll(null, rightScroll)
                    }
                    // else scroll to start
                } else {
                    val scrollBy = scrollTo - timeLineScrollPosition
                    scrollTimeHeader(null, scrollBy)
                    scrollAll(null, scrollBy)
                    updateTimeIndicator()
                }
            }


            val showView = channelRecycler.findViewWithTag<View>(tag) ?: return@postDelayed
            lastSelectedShowView?.isSelected = false
            lastSelectedShowView = showView
            showView.isSelected = true
            listener?.onShowSelected(channel.id, show.id)
        }, EPGConfig.focusDelay)
    }

    @Suppress("unused")
    fun scrollToNow() {
        post {
            val nowOffset = if (dayShift == 0) getCellWidth(
                startTime, DateTime()
            ) - binding.rvChannels.width / 2
            else 0
            val scrollBy = nowOffset - timeLineScrollPosition
            scrollTimeHeader(null, scrollBy)
            scrollAll(null, scrollBy)
            updateTimeIndicator()
        }
    }

    private fun setTimeHeader() {
        val marginStep = getDayLength() * minuteToPixel / maxHour

        val hours = mutableListOf<DataModel.TimeLineDataModel>()
        for (hourIndex in 0..maxHour) {
            val gravity = when (hourIndex) {
                0 -> Gravity.START
                maxHour -> Gravity.END
                else -> Gravity.CENTER
            }
            hours.add(
                DataModel.TimeLineDataModel(
                    timeId = hourIndex.toString(),
                    time = String.format("%02d:00", (startTime.hourOfDay + hourIndex) % 24),
                    gravity = gravity,
                    marginStart = when (hourIndex) {
                        0 -> 0
                        1 -> marginStep - timeLabelWidth - timeLabelWidth / 2
                        else -> marginStep - timeLabelWidth
                    }
                )
            )
        }
        timeAdapter.submitList(hours.toList())
    }

    private fun setChannelsLogo(items: List<DataModel>) {
        channelsLogoAdapter.submitList(items)
    }

    private fun setChannels(items: List<DataModel>) {
        (binding.rvChannels.adapter as? ChannelListAdapter)?.submitList(items)
    }

    private fun scrollAll(recyclerView: RecyclerView?, dx: Int) {
        val rvChildren =
            binding.rvChannels.children.map { it as? RecyclerWithPositionView }.toList()
        rvChildren.forEach {
            if (recyclerView != it) it?.scrollHorizontallyBy(dx)
        }
    }

    private fun scrollTimeHeader(recyclerView: RecyclerView?, dx: Int) {
        timeLineScrollPosition = max(timeLineScrollPosition + dx, 0)
        currentEpgTime = startTime.plusMinutes(timeLineScrollPosition / minuteToPixel)
        updateTimeIndicator()
        if (binding.rvTimeLine != recyclerView) {
            binding.rvTimeLine.apply {
                clearOnScrollListeners()
                scrollBy(dx, 0)
                addOnScrollListener(horizontalScrollListener)
            }
        }
    }

    private fun scrollChannels(recyclerView: RecyclerView?, dy: Int) {
        if (binding.rvChannels != recyclerView) {
            binding.rvChannels.apply {
                clearOnScrollListeners()
                scrollBy(0, dy)
                addOnScrollListener(verticalScrollListener)
            }
        }
    }

    private fun scrollChannelsLogo(recyclerView: RecyclerView?, dy: Int) {
        if (binding.rvChannelsLogos != recyclerView) {
            binding.rvChannelsLogos.apply {
                clearOnScrollListeners()
                scrollBy(0, dy)
                addOnScrollListener(verticalScrollListener)
            }
        }
    }

    private fun List<ChannelModel>.mapChannels(): List<DataModel> {
        return this.map { channel ->
            DataModel.ChannelDataModel(channelId = channel.id,
                channelName = channel.name,
                logo = channel.logo,
                shows = channel.shows,
                onShowClick = { showId ->
                    listener?.onShowClick(channelId = channel.id, showId = showId)
                })
        }
    }

    private fun List<DummyModel>.mapDummyItems(): List<DataModel> {
        return this.mapIndexed { index, dummy ->
            DataModel.DummyChannel(
                title = dummy.title, dummyIndex = index, onClick = dummy.onClick
            )
        }
    }

    private fun List<ShowModel>.getCurrentShow(): ShowModel? {
        return this.find { it.startDate.isBeforeNow && it.endDate.isAfterNow }
    }
}