package com.volkov.epgrecycler

import org.joda.time.DateTime
import org.joda.time.Hours
import org.joda.time.Minutes
import org.joda.time.Seconds


object EPGUtils {

    const val SHOW_TIME_PATTERN = "HH:mm"
    private const val MINUTE_TO_PIXEL = 5
    private const val TIME_LABEL_WIDTH = 40

    var dayShift = 0
    var startHour = 0

    var currentEpgTime: DateTime = startTime

    val minuteToPixel: Int
        get() = MINUTE_TO_PIXEL.dpToPx

    val timeLabelWidth: Int
        get() = TIME_LABEL_WIDTH.dpToPx

    val maxHour: Int
        get() = Hours.hoursBetween(startTime, endTime).hours

    fun getCellWidth(from: DateTime, to: DateTime): Int =
        Minutes.minutesBetween(from, to).minutes * minuteToPixel

    fun getCellWidthSeconds(from: DateTime, to: DateTime): Int =
        Seconds.secondsBetween(from, to).seconds / 60 * minuteToPixel

    fun getDayLength(): Int {
        return Minutes.minutesBetween(startTime, endTime).minutes
    }

    val startTime: DateTime
        get() = DateTime()
            .plusDays(dayShift)
            .withHourOfDay(startHour)
            .withMinuteOfHour(0)
            .withSecondOfMinute(0)
            .withMillisOfSecond(0)

    val endTime: DateTime
        get() = startTime.plusDays(1)
}