package com.volkov.epgrecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
            displayFirstShowIcon = true
            marginTop = 8
            marginStart = 8
            rowHeight = 50
        }
        binding.epgView.apply {
            setDayShift(0)
            val startDate = DateTime()
            setStartHour(startDate.hourOfDay - 1)
            initView(
                listOf(
                    ChannelModel(
                        id = 1,
                        logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg",
                        name = "Channel_1",
                        shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = 1,
                                name = "The Simpsons",
                                startDate = startDate,
                                endDate = startDate.plusMinutes(50),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            ),
                            ShowModel(
                                id = "show_2",
                                channelId = 1,
                                name = "The Simpsons 2",
                                startDate = startDate.plusMinutes(50),
                                endDate = startDate.plusMinutes(140),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    )
                )
            )
        }
    }
}