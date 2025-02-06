package com.constant.japaneseeveryday.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Process
import android.view.View
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.util.Coordinate

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class DebugStatusView : View {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    private val HEIGHT = 100

    // Public Variable
    public var debugText: String = "debugText"

    // ----------------------------------------------------
    // Private Variable
    private val paint: Paint
    private val bgPaint: Paint
    private val processId: Int

    // Override Method or Basic Method

    constructor(context: Context) : super(context) {
        paint = Paint()
        paint.color = context.resources.getColor(R.color.blue)
        paint.textSize = context.resources.getDimension(R.dimen.font_p1_size)
        paint.isAntiAlias = true
        paint.textAlign = Paint.Align.CENTER

        bgPaint = Paint()
        bgPaint.color = context.resources.getColor(R.color.fg_brand)

        processId = Process.myPid()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (canvas == null) {
            return
        }
        // canvas.drawRect(0f, 0f, 300f, 300f, mBGPaint)

        canvas.drawText(
            "PID:" + processId,
            Coordinate.getWidth().toFloat() / 2,
            (HEIGHT / 2).toFloat(),
            paint,
        )
    }

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
    ) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(Coordinate.getWidth(), HEIGHT)
    }
    // Public Method
    // Private Method
}
