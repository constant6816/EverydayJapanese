package com.constant.japaneseeveryday.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.constant.japaneseeveryday.R

class AttendanceView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : View(context, attrs, defStyleAttr) {
        private var startSecs: Int = 0
        private var endSecs: Int = 0
        private var fgColor: Int = 0
        private var bgColor: Int = 0
        private var lineCap: Paint.Cap = Paint.Cap.ROUND

        private val paint: Paint = Paint()

        init {
            initializeViews()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val padding = 4.0f
            val lineWidth = 10.0f
            val width = width - padding * 2

            // Paint 설정
            paint.strokeWidth = lineWidth
            paint.strokeCap = lineCap

            // 뒷배경 그리기
            paint.color = bgColor
            canvas.drawLine(padding, height / 2.0f, width, height / 2.0f, paint)

            // 활성 라인 그리기
            if (startSecs != endSecs) {
                val start = padding + width * startSecs / (24 * 60 * 60)
                val end = padding + width * endSecs / (24 * 60 * 60)

                paint.color = fgColor
                canvas.drawLine(start, height / 2.0f, end, height / 2.0f, paint)
            }
        }

        fun setValue(
            startSecs: Int,
            endSecs: Int,
        ) {
            if (this.startSecs == startSecs && this.endSecs == endSecs) {
                return
            }
            this.startSecs = startSecs
            this.endSecs = endSecs
            invalidate() // setNeedsDisplay()에 대응
        }

        fun setColor(
            bg: Int,
            fg: Int,
        ) {
            bgColor = bg
            fgColor = fg
            invalidate()
        }

        fun setLineCap(lineCap: Paint.Cap) {
            this.lineCap = lineCap
            invalidate()
        }

        private fun initializeViews() {
            setBackgroundColor(Color.TRANSPARENT)
            fgColor = context.getColor(R.color.fg0)
            bgColor = context.getColor(R.color.bg3)
        }
    }
