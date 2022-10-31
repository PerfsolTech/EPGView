package com.volkov.epgrecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.epgrecycler.models.epg.ChannelModel
import com.volkov.epgrecycler.models.epg.ShowModel
import com.volkov.epgrecyclerview.databinding.ActivityMainBinding
import org.joda.time.DateTime

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.epgView.apply {
            initView(
                listOf(
                    ChannelModel(
                        id = 1, logo = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg", name = "Channel_1", shows = listOf(
                            ShowModel(
                                id = "show_1",
                                channelId = 1,
                                name = "The Simpsons",
                                startDate = DateTime(),
                                endDate = DateTime().plusMinutes(90),
                                showPreviewImage = "https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
                            )
                        )
                    )
                )
            )
            setStartHour(5)
            setDayShift(0)
        }
    }
}