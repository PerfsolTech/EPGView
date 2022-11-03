package com.volkov.epgrecycler

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.epg_recycler.R
import com.volkov.epg_recycler.databinding.ViewEpgRecyclerBinding
import com.volkov.epgrecycler.Constants.TIME_HEADER
import com.volkov.epgrecycler.EPGUtils.currentEpgTime
import com.volkov.epgrecycler.EPGUtils.dayShift
import com.volkov.epgrecycler.EPGUtils.endTime
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
import com.volkov.epgrecycler.models.epg.ShowModel
import org.joda.time.DateTime
import org.joda.time.Minutes
import kotlin.math.max

class EPGRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding by viewBinding(ViewEpgRecyclerBinding::bind)

    private var channels: List<ChannelModel> = emptyList()
    private val channelsMap = mutableMapOf<Int, Pair<String, String>>()

    private var lastSelectedShowView: View? = null

    interface OnEventListener {
        fun onShowSelected(channelId: Int, showId: String)
        fun onShowClick(channelId: Int, showId: String)
        fun onShowExit()
    }

    var listener: OnEventListener? = null

    enum class MoveDirection {
        UP, DOWN, LEFT, RIGHT, NONE
    }

    private var lastDirection = MoveDirection.NONE

    private val horizontalScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            scrollAll(recyclerView, dx)
            scrollTimeHeader(recyclerView, dx)
            updateTimeIndicator(withSubmit = false)
        }
    }

    fun setStartHour(startHour: Int) {
        EPGUtils.startHour = startHour
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
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    when (lastDirection) {
                        MoveDirection.UP -> listener?.onShowExit()
                        MoveDirection.DOWN -> lastSelectedShowView?.apply {
                            post {
                                requestFocus()
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }

        binding.rvChannelsLogos.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = channelsLogoAdapter
            setHasFixedSize(true)
            itemAnimator = null
            addOnScrollListener(verticalScrollListener)
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    val ar = lastSelectedShowView?.tag?.toString()?.split("#")
                        ?: return@setOnFocusChangeListener
                    val channelId = ar[0].toInt()
                    val showId = ar[1]
                    val currentChannel = channels.single { it.id == channelId }
                    val currentShow = currentChannel.shows.singleOrNull { it.id == showId }
                    val foundingShow = when (lastDirection) {
                        MoveDirection.UP -> {
                            currentShow?.let {
                                (channels.getOrNull(channels.indexOf(currentChannel) - 1)
                                    ?: channels.firstOrNull())
                                    ?.findShowStartAfterTime(
                                        currentShow.startDate.plusMinutes(currentShow.showDuration / 2)
                                    )
                            }
                        }
                        MoveDirection.DOWN -> {
                            currentShow?.let {
                                channels.getOrNull(channels.indexOf(currentChannel) + 1)
                                    ?.findShowStartAfterTime(
                                        currentShow.startDate.plusMinutes(currentShow.showDuration / 2)
                                    )
                            }
                        }
                        else -> null
                    }
                    foundingShow?.let { show ->
                        val rv =
                            binding.rvChannels.findViewWithTag<RecyclerView>("channel_${show.channelId}")
                        rv?.post {
                            val children = rv.children.toList()
                            val view =
                                rv.findViewWithTag<View>(show.showTag).takeIf { it.isViewOnScreen }
                                    ?: children.getOrNull(children.size - 2)
                            view?.requestFocus()
                        }
                    } ?: run {
                        (binding.rvChannels.children.firstOrNull() as? RecyclerView)
                            ?.children?.firstOrNull()?.requestFocus()
                    }
                }
            }
        }
        binding.root.viewTreeObserver.addOnGlobalFocusChangeListener { oldFocus, newFocus ->
            if (oldFocus != null) {
                val newId =
                    runCatching { newFocus?.id?.let { resources.getResourceEntryName(it) } }
                        .getOrDefault("")
                if (newId != "show_parent") return@addOnGlobalFocusChangeListener
                if (newFocus.isViewOnScreen) lastSelectedShowView = newFocus
            }
        }
    }

    fun initView(channels: List<ChannelModel>) {
        setTimeHeader()
        if (channels.isEmpty()) return
        this.channels = channels.map { it.copy(shows = it.shows.filterShows()) }
        initChannelRecycler()
        val mappedChannels = this.channels.mapChannels()
        setChannelsLogo(mappedChannels)
        setChannels(mappedChannels)
        scrollToNow()
        selectCurrentShow(channels.first().id)
    }

    private fun ChannelModel.findShowStartAfterTime(time: DateTime): ShowModel? {
        return this.shows.find { show ->
            show.startDate == time
                    || (show.startDate.isBefore(time) && show.endDate.isAfter(time))
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val focusedView: View?
        val id: String?
        val tag: String?
        var channelId: Int? = null
        var showId: String? = null
        if (event.action == KeyEvent.ACTION_DOWN) {
            focusedView = findFocus()
            id = resources.getResourceEntryName(focusedView.id)
            tag = focusedView.tag?.toString()
            if (id == "show_parent") {
                tag?.split("#")?.apply {
                    channelId = first().toInt()
                    showId = this[1]
                }
            }
        }
        return when {
            event.onLeftPressed() -> {
                lastDirection = MoveDirection.LEFT
                channelId?.let {
                    channelsMap[it]?.first == showId
                } ?: false
            }
            event.onRightPressed() -> {
                lastDirection = MoveDirection.RIGHT
                channelId?.let {
                    channelsMap[it]?.second == showId
                } ?: false
            }
            event.onUpPressed() -> {
                lastDirection = MoveDirection.UP
                false
            }
            event.onDownPressed() -> {
                lastDirection = MoveDirection.DOWN
                false
            }
            else -> super.dispatchKeyEvent(event)
        }
    }

    private fun initChannelRecycler() {
        binding.rvChannels.apply {
            val channelListAdapter = ChannelListAdapter(horizontalScrollListener, listener)
            layoutManager = object : LinearLayoutManager(context) {
                override fun onInterceptFocusSearch(focused: View, direction: Int): View? = null
            }
            adapter = channelListAdapter
            setHasFixedSize(true)
            addOnScrollListener(verticalScrollListener)
        }
    }

    fun updateTimeIndicator(withSubmit: Boolean = true) {
        val now = DateTime()
        val indicatorPosition = getCellWidthSeconds(startTime, now)
        val isVisible = indicatorPosition - timeLineScrollPosition in (0..binding.rvTimeLine.width)
        binding.timeIndicator.isVisible = isVisible
        binding.timeIndicator.updateLayoutParams<LayoutParams> {
            marginStart = getCellWidthSeconds(currentEpgTime, now)
        }
        if (!withSubmit) return
        binding.rvChannels.children.toList().map { it as RecyclerView }.forEach {
            it.children.toList().map { view -> view.tag?.toString() ?: "" }.forEach { tag ->
                if (tag.isNotEmpty() && tag.contains("#")) {
                    val v = tag.split("#")
                    val channelId = v.first().toInt()
                    channels.singleOrNull { channel -> channel.id == channelId }?.let { channel ->
                        val currentShow = channel.shows.getCurrentShow()
                        findViewWithTag<View>(tag).isActivated = currentShow?.id == v[1]
                    }
                }
            }
        }
    }

    private fun selectCurrentShow(channelId: Int) {
        val channel = channels.singleOrNull { it.id == channelId } ?: return
        val currentShow = channel.shows.getCurrentShow()
        val tag = "${channelId}#${currentShow?.id}"
        postDelayed({
            binding.rvChannels.children.toList().firstOrNull()?.findViewWithTag<View>(tag)
                ?.requestFocus()
        }, 50)
    }

    fun scrollToNow() {
        if (dayShift != 0) return
        post {
            val nowOffset = getCellWidth(startTime, DateTime()) - binding.rvChannels.width / 2
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

    private fun setChannelsLogo(channels: List<DataModel.ChannelDataModel>) {
        channelsLogoAdapter.submitList(channels)
    }

    private fun setChannels(channels: List<DataModel.ChannelDataModel>) {
        (binding.rvChannels.adapter as? ChannelListAdapter)?.submitList(channels)
    }

    private fun scrollAll(recyclerView: RecyclerView?, dx: Int) {
        val rvChildren = binding.rvChannels.children.map { it as RecyclerWithPositionView }.toList()
        rvChildren.forEach {
            if (recyclerView != it) it.scrollHorizontallyBy(dx)
        }
    }

    private fun scrollTimeHeader(recyclerView: RecyclerView?, dx: Int) {
        timeLineScrollPosition = max(timeLineScrollPosition + dx, 0)
        currentEpgTime = startTime.plusMinutes(timeLineScrollPosition / minuteToPixel)
        binding.tvCurrentTime.text = currentEpgTime.toString("EEE, dd MMM HH:mm")
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

    private fun List<ShowModel>.filterShows(): List<ShowModel> {
        return this.filter {
            it.startDate.isBefore(endTime) && it.endDate.isAfter(DateTime())
        }
    }

    private fun List<ChannelModel>.mapChannels(): List<DataModel.ChannelDataModel> {
        return this.map { channel ->
            if (channel.shows.isNotEmpty()) {
                channelsMap[channel.id] = Pair(channel.shows.first().id, channel.shows.last().id)
            }
            DataModel.ChannelDataModel(
                channelId = channel.id,
                channelName = channel.name,
                logo = channel.logo,
                shows = channel.shows
            )
        }
    }

    private fun List<ShowModel>.getCurrentShow(): ShowModel? {
        return this.find { it.startDate.isBeforeNow && it.endDate.isAfterNow }
    }

    private val ShowModel.showDuration: Int
        get() = Minutes.minutesBetween(startDate, endDate).minutes
}