package com.constant.japaneseeveryday.view.calendar

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.constant.japaneseeveryday.extension.LocalDateTimeExtensions
import com.constant.japaneseeveryday.extension.beginningOfMonth
import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.extension.toMilli
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class MonthlyAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private var start: LocalDateTime = LocalDateTimeExtensions.today().beginningOfMonth()
    private var schedules: ArrayList<DateStatus> = ArrayList<DateStatus>()

    // Override Method or Basic Method
    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): MonthlyFragment {
        HHLog.d(TAG, "createFragment($position)")
        val millis = getItemId(position)
        HHLog.d(TAG, "millis = $millis")

        val dateTime = millis.toLocalDateTime()
        val year = dateTime.year
        val month = dateTime.monthValue
        HHLog.d(TAG, "year = $year, month = $month")
        return MonthlyFragment.newInstance(year, month, schedules)
    }

    override fun getItemId(position: Int): Long = start.plusMonths(position.toLong() - START_POSITION).toMilli()

    override fun containsItem(itemId: Long): Boolean {
        val dateTime = itemId.toLocalDateTime()
        return dateTime.dayOfMonth == 1
    }

    // Public Method
    fun setSchedules(schedules: ArrayList<DateStatus>) {
        this.schedules = schedules
    }
    // Private Method
}
