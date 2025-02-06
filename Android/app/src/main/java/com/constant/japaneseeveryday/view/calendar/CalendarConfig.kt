package com.constant.japaneseeveryday.view.calendar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.Date

object CalendarConfig {
    const val daysInWeek = 7
    const val monthlyWeek = 6
}

interface CalendarViewDelegate {
    fun didSelect(date: Date)

    fun didUnselect()

    fun movedToOtherMonth()
}

@Parcelize
class DateStatus(val date: LocalDateTime, val seconds: Int) : Parcelable
