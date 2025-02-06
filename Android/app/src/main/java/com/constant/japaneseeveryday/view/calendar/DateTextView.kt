package com.constant.japaneseeveryday.view.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.TextView
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.extension.applyGUI
import com.constant.japaneseeveryday.extension.isSameMonth
import com.constant.japaneseeveryday.extension.isToday
import com.constant.japaneseeveryday.util.nonNull
import java.time.LocalDateTime

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class DateTextView : TextView {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    private val todayWidth = 16f
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    lateinit var monthDate: LocalDateTime

    // Private Variable
    private var date: LocalDateTime = LocalDateTime.now()
    private var isSelected: Boolean = false

    private var todayPaint: Paint? = null
    private var selectedPaint: Paint? = null

    private val isToday: Boolean
        get() {
            return date.isToday()
        }

    // Override Method or Basic Method
    constructor(context: Context) : super(context) {
        initializeViews()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initializeViews()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr,
    ) {
        initializeViews()
    }

    override fun onDraw(canvas: Canvas) {
        if (isToday) {
            canvas.drawOval(
                RectF(
                    width.toFloat() / 2 + 34 - todayWidth,
                    0f,
                    width.toFloat() / 2 + 34,
                    todayWidth,
                ),
                todayPaint!!,
            )
        }
        if (isSelected) {
            val padding = 4.0f
            canvas.drawLine(
                padding,
                height.toFloat(),
                width.toFloat() - 2 * padding,
                height.toFloat(),
                selectedPaint!!,
            )
        }
        super.onDraw(canvas)
    }

    // Public Method
    fun setDate(date: LocalDateTime) {
        this.date = date
        updateView()
    }

    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
        updateView()
    }

    fun getIsSelected(): Boolean {
        return isSelected
    }

    // Private Method
    private fun initializeViews() {
        todayPaint = Paint()
        todayPaint!!.color = getContext().getResources().getColor(R.color.fg_brand)
        todayPaint!!.style = Paint.Style.FILL

        selectedPaint = Paint()
        selectedPaint!!.color = getContext().getResources().getColor(R.color.fg_brand_light)
        selectedPaint!!.strokeWidth =
            getContext().getResources().getDimension(R.dimen.selected_date_stroke_width)
        selectedPaint!!.style = Paint.Style.STROKE
    }

    private fun updateView() {
        text = date.dayOfMonth.toString()
        if (monthDate.isSameMonth(date)) {
            applyGUI(R.style.font_p2, R.color.fg1)
        } else {
            applyGUI(R.style.font_p2, R.color.fg6)
        }
    }
}
