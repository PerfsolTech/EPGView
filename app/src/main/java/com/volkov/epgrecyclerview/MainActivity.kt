package com.volkov.epgrecyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.volkov.epgrecycler.models.epg.ChannelModel
import com.volkov.epgrecyclerview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.epgView.apply {
            initView(
                listOf(
                    ChannelModel(1, null, "Channel_1", emptyList())
                )
            )
            setStartHour(5)
            setDayShift(0)
        }
    }
}