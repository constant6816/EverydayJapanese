package com.constant.japaneseeveryday.view.calendar
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class DaySymbolsView : LinearLayout {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable

    private val labels: MutableList<TextView> = mutableListOf()

    private val TAG = nonNull(this::class.simpleName)

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

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
    ) {
        super.onLayout(changed, left, top, right, bottom)
        val labelWidth = (width / CalendarConfig.daysInWeek).toInt()

        labels.forEachIndexed { index, label ->
            val xPosition = (labelWidth * index).toInt()
            HHLog.d(TAG, "xPosition = $xPosition")
            label.layout(xPosition, 0, xPosition + labelWidth, height)
//            if (index % 2 == 0) {
//                label.setBackgroundColor(R.color.fg_brand)
//            }
        }
    }

    // Public Method
    // Private Method
    private fun initializeViews() {
        orientation = HORIZONTAL

        val labelWidth = (width / CalendarConfig.daysInWeek).toInt()
        for (i in 0..<CalendarConfig.daysInWeek) {
            val params = LinearLayout.LayoutParams(labelWidth, labelWidth)
            params.setMargins(0, 0, 0, 0)
            params.gravity = android.view.Gravity.CENTER_HORIZONTAL

            val label = TextView(context)
            label.textAlignment = View.TEXT_ALIGNMENT_CENTER
            label.gravity = Gravity.CENTER
            label.layoutParams = params
            labels.add(label)
            addView(label)
        }

        val daySymbols =
            listOf(
                context.resources.getString(R.string.sunday),
                context.resources.getString(R.string.monday),
                context.resources.getString(R.string.tuesday),
                context.resources.getString(R.string.wednesday),
                context.resources.getString(R.string.thursday),
                context.resources.getString(R.string.friday),
                context.resources.getString(R.string.saturday),
            )

        var weekDays = daySymbols.toMutableList()
//        val shiftAmount = calendar.firstDayOfWeek - java.util.Calendar.SUNDAY
//        weekDays = weekDays.drop(shiftAmount) + weekDays.take(shiftAmount)

        labels.forEachIndexed { index, label ->
            label.text = weekDays[index]
            // label.applyGUI(font = FontType.TYPE_AP2, textColor = resources.getColor(R.color.fg4))
        }
    }
}
