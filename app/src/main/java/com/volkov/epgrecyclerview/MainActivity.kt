package com.volkov.epgrecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.EPGConfig
import com.volkov.epgrecycler.models.epg.ChannelModel
import com.volkov.epgrecycler.models.epg.ShowModel
import com.volkov.epgrecyclerview.databinding.ActivityMainBinding
import org.joda.time.DateTime

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(EPGConfig) {
            marginTop = 8
            marginEnd = 8
            rowHeight = 50
            showDelimiter = false
        }
        binding.epgView.apply {
            setDayShift(0)
            val startDate = DateTime().minusMinutes(10)
            val endTime = startDate.plusMinutes(11)
            setStartHour(startDate.hourOfDay - 1)
            setEndHour(0)
            initView(
                listOf(
                    ChannelModel(
                        id = "1",
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    ),
                    ChannelModel(
                        id = "1",
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    ),
                    ChannelModel(
                        id = "1",
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    ),
                    ChannelModel(
                        id = "1",
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    ), ChannelModel(
                        id = "1",
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    ), ChannelModel(
                        id = "1",
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    ), ChannelModel(
                        id = "1",
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    ), ChannelModel(
                        id = "1",
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = "1",
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = endTime,
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = "1",
                                name = "The Simpsons 2",
                                startDate = endTime,
                                endDate = endTime.plusMinutes(20),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    )
                )
            )
        }
    }
}