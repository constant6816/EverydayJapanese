package com.constant.japaneseeveryday.view

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class ColorDivider(
    private val height: Float,
    private val padding: Float,
    @ColorInt
    private val color: Int,
    private val includeSeparators: (position: Int) -> Boolean,
) : RecyclerView.ItemDecoration() {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    private val paint = Paint()

    // Override Method or Basic Method
    init {
        paint.color = color
    }

    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val left = parent.paddingStart + padding
        val right = parent.width - parent.paddingEnd - padding

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = (child.bottom + params.bottomMargin).toFloat()
            val bottom = top + height
            if (includeSeparators(position)) {
                c.drawRect(left, top, right, bottom, paint)
            }
        }
    }
    // Public Method
    // Private Method
}
