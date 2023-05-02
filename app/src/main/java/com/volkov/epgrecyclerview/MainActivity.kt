package com.volkov.epgrecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.EPGConfig
import com.volkov.epgrecycler.EPGRecyclerView
import com.volkov.epgrecycler.models.epg.ChannelModel
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
            marginTop = 8
            marginEnd = 8
            rowHeight = 50
            showDelimiter = false
        }
        binding.btnFocus.setOnClickListener {
            Timber.e("Test")
        }
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
                        logo = "https://fastly.picsum.photos/id/738/300/200.jpg?hmac=aCujhxZ8Lq6Sj8d3iYFTYPBMrqZ9AHNKWCe7Kylddes",
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
                        logo = "https://fastly.picsum.photos/id/929/300/200.jpg?hmac=8Wtfz6VsHDZp-UtwHLeemRB_8Qo2AIueepVpR0_vvSw",
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
                    ),
                    ChannelModel(
                        id = "3",
                        logo = "https://fastly.picsum.photos/id/893/300/200.jpg?hmac=YBp4mTpSWT0C7-homNgHQSkkiiN27b6f-qD936sPLAE",
                        name = "Channel_3",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "3",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://picsum.photos/200/300"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "3",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://picsum.photos/200/300"
                            )
                        )
                    ),
                    ChannelModel(
                        id = "4",
                        logo = "https://fastly.picsum.photos/id/309/300/200.jpg?hmac=apQkn0hchHXEpqW4ul2aoeT2Ofx-pbazeJ_Psws9Nz8",
                        name = "Channel_4",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "4",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://picsum.photos/200/300"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "4",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://picsum.photos/200/300"
                            )
                        )
                    ), ChannelModel(
                        id = "5",
                        logo = "https://fastly.picsum.photos/id/722/300/200.jpg?hmac=02ikjsAWa_35iOnQ01XPy3o6bXyQovQi2Ou1hdRk_34",
                        name = "Channel_5",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "5",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://picsum.photos/200/300"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "5",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://picsum.photos/200/300"
                            )
                        )
                    ), ChannelModel(
                        id = "6",
                        logo = "https://fastly.picsum.photos/id/966/300/200.jpg?hmac=cot5CVXjVGVvCXYRmxhp_A8ELZjuwQBmg2rCilTgfFg",
                        name = "Channel_6",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "6",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://picsum.photos/200/300"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "6",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://picsum.photos/200/300"
                            )
                        )
                    ), ChannelModel(
                        id = "7",
                        logo = "https://fastly.picsum.photos/id/198/300/200.jpg?hmac=tklm6CzIgRqZX66BjwFARM05cLtx4iUCSwzmz75qRzA",
                        name = "Channel_7",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "7",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://picsum.photos/200/300"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "7",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://picsum.photos/200/300"
                            )
                        )
                    ), ChannelModel(
                        id = "8",
                        logo = "https://fastly.picsum.photos/id/5/300/200.jpg?hmac=OEThX-GUhKMPjHoNILLrmIrAOlqx2XdgXqUf4FMNx5E",
                        name = "Channel_8",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "8",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://picsum.photos/200/300"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "8",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://picsum.photos/200/300"
                            )
                        )
                    )
                )
            )
        }
    }
}