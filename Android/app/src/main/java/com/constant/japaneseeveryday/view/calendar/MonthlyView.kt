package com.constant.japaneseeveryday.view.calendar
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.extension.beginningOfMonth
import com.constant.japaneseeveryday.extension.isSameMonth
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime
import java.util.*

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
interface MonthlyViewDelegate {
    fun didSelect(date: Date)

    fun didUnselect()
}

class MonthlyView : FrameLayout {
    // Public Inner Class, Struct, Enum, Interface
    inner class DayAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textviewDay: DateTextView = itemView.findViewById(R.id.textview_day)
            val viewStatus: View = itemView.findViewById(R.id.view_status)

            fun bind(date: LocalDateTime) {
                HHLog.d(TAG, "bind() date = $date")
                textviewDay.monthDate = monthDate
                textviewDay.setDate(date)
                val workSeconds = getWorkSeconds(date, schedules)
                if (workSeconds != null && monthDate.isSameMonth(date)) {
                    HHLog.d(TAG, "img_circle date = $date")
                    viewStatus.visibility = View.VISIBLE

                    val hour: Int = 60 * 60
                    val tintColor: Int
                    when (workSeconds) {
                        0 -> {
                            tintColor = context.getColor(R.color.clear)
                        }
                        in 1 until hour -> {
                            tintColor = context.getColor(R.color.fg_green_0)
                        }
                        in hour until 2 * hour -> {
                            tintColor = context.getColor(R.color.fg_green_1)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_0))
                        }
                        in 2 * hour until 3 * hour -> {
                            tintColor = context.getColor(R.color.fg_green_2)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_1))
                        }
                        in 3 * hour until 4 * hour -> {
                            tintColor = context.getColor(R.color.fg_green_3)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_2))
                        }
                        in 4 * hour until 5 * hour -> {
                            tintColor = context.getColor(R.color.fg_green_4)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_3))
                        }
                        in 5 * hour until 6 * hour -> {
                            tintColor = context.getColor(R.color.fg_green_5)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_4))
                        }
                        in 6 * hour until 7 * hour -> {
                            tintColor = context.getColor(R.color.fg_green_6)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_5))
                        }
                        in 7 * hour until 8 * hour -> {
                            tintColor = context.getColor(R.color.fg_green_7)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_6))
                        }
                        in 8 * hour until 9 * hour -> {
                            tintColor = context.getColor(R.color.fg_green_8)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_7))
                        }
                        in 9 * hour..Int.MAX_VALUE -> {
                            tintColor = context.getColor(R.color.fg_green_9)
                            // viewStatus.setBackgroundColor(AttendanceApplication.context.getColor(R.color.fg_green_8))
                        }
                        else -> {
                            tintColor = context.getColor(R.color.clear)
                            HHLog.d(TAG, "Unhandled case")
                        }
                    }
                    ViewCompat.setBackgroundTintList(viewStatus, ColorStateList.valueOf(tintColor))
                    if (6 * hour < workSeconds) {
                        textviewDay.setTextColor(context.getColor(R.color.fg0))
                    } else if (1 < workSeconds && workSeconds <= 6 * hour){
                        textviewDay.setTextColor(context.getColor(R.color.bg0))
                    }
                } else {
                    HHLog.d(TAG, "no img_circle date = $date")
                    viewStatus.visibility = View.GONE
                    // textviewDay.setTextColor(context.getColor(R.color.fg_dark6))
                }
                // textviewDay.setIsSelected(true)

                itemView.setOnClickListener {
                    HHLog.d(TAG, "onClick()")
                    // if (!monthDate.isSameMonth(date)) return@setOnClickListener

//                    if (selectedDate == null) {
//                        GlobalSubject.getInstance().selectDaySubject.onNext(date)
//                        selectedDate = date
//                        textviewDay.setIsSelected(true)
//                    } else {
//                        GlobalSubject.getInstance().selectDaySubject.onNext(null)
//                        selectedDate = null
//                        textviewDay.setIsSelected(false)
//                    }
                    // calendarView.reloadAll()
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): DayViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.grid_day, parent, false)
            return DayViewHolder(view)
        }

        override fun getItemCount(): Int {
            return CalendarConfig.daysInWeek * CalendarConfig.monthlyWeek
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            var date = startDate.plusDays(position.toLong())
            val dayViewHolder: DayViewHolder = holder as DayViewHolder
            HHLog.d(TAG, "date = $date")
            dayViewHolder.bind(date)
        }
    }

    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    private lateinit var schedules: ArrayList<DateStatus>
    private lateinit var monthDate: LocalDateTime
    lateinit var recyclerView: RecyclerView
    var delegate: MonthlyViewDelegate? = null
    private var selectedDate: LocalDateTime? = null

    // Private Variable
    private val startDate: LocalDateTime
        get() {
            val weekday = monthDate.dayOfWeek.value
            return monthDate.minusDays(weekday.toLong())
        }

    // Override Method or Basic Method
    constructor(context: Context) : super(context) {
        initializeViews()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initializeViews()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        initializeViews()
    }

    // Public Method
    fun setSchedules(schedules: ArrayList<DateStatus>) {
        this.schedules = schedules
    }

    fun setMonthDate(monthDate: LocalDateTime) {
        this.monthDate = monthDate
        recyclerView.adapter?.notifyDataSetChanged()
    }

    // Private Method
    private fun initializeViews() {
        if (context == null) {
            return
        }
        monthDate = LocalDateTime.now().beginningOfMonth()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        inflater?.inflate(R.layout.view_monthly, this, true)
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = GridLayoutManager(context, CalendarConfig.daysInWeek)
        recyclerView.adapter = DayAdapter()
    }

    private fun getWorkSeconds(
        date: LocalDateTime,
        dateStatuses: ArrayList<DateStatus>,
    ): Int? {
        for (dateStatus in dateStatuses) {
            val year1 = date.year
            val month1 = date.monthValue
            val day1 = date.dayOfMonth
            val year2 = dateStatus.date.year
            val month2 = dateStatus.date.monthValue
            val day2 = dateStatus.date.dayOfMonth
            if (year1 == year2 && month1 == month2 && day1 == day2) {
                return dateStatus.seconds
            }
        }
        return null
    }
}
