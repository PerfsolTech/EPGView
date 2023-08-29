package com.volkov.epgrecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.EPGConfig
import com.volkov.epgrecycler.EPGRecyclerView
import com.volkov.epgrecycler.models.epg.ChannelModel
import com.volkov.epgrecycler.models.epg.DummyModel
import com.volkov.epgrecycler.models.epg.ShowModel
import com.volkov.epgrecyclerview.databinding.ActivityMainBinding
import org.joda.time.DateTime
import timber.log.Timber

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    private val epgListener = object : EPGRecyclerView.OnEventListener {
        override fun onShowSelected(channelId: String, showId: String) {}

        override fun onShowClick(channelId: String, showId: String) {
            Timber.e("click: $channelId, $showId")
        }

        override fun onShowExit() {
            binding.btnFocus.post {
                binding.btnFocus.requestFocus()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(EPGConfig) {
            rowHeight = 50
            rowLogoHeight = 40
            marginVerticalChannelLogo = 5
        }
        EPGConfig.channelLogoBackgroundDrawable = com.volkov.epg_recycler.R.drawable.bg_radius_4dp
        binding.epgView.apply {
            setDayShift(0)
            val startDate = DateTime().minusMinutes(10)
            val endTime = startDate.plusMinutes(11)
            setStartHour(startDate.hourOfDay - 1)
            setEndHour(0)
            listener = epgListener
            initView(
                listOf(
                    ChannelModel(
                        id = "1",
                        logo = "https://logo.tv.minick.net/mobiletv/dns-net/150x150/150x150_27.png",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://picsum.photos/200/300"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://picsum.photos/200/300"
                            )
                        )
                    ),
                    ChannelModel(
                        id = "2",
                        logo = "https://logo.tv.minick.net/mobiletv/dns-net/150x150/150x150_27.png",
                        name = "Channel_2",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "2",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://picsum.photos/200/300"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "2",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://picsum.photos/200/300"
                            )
                        )
                    )
                ),
                dummyItems = listOf(
                    DummyModel("Test") { Timber.e("Test") },
                    DummyModel("Test2") { Timber.e("Test2") },
                    DummyModel("Test3") { Timber.e("Test3") }),
            )
        }
    }
}