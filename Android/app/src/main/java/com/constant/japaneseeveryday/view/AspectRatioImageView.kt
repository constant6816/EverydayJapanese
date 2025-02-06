package com.constant.japaneseeveryday.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class AspectRatioImageView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : ImageView(context, attrs, defStyleAttr) {
        private var aspectRatio = 2.0f // 기본 비율 2:1

        fun setAspectRatio(aspectRatio: Float) {
            if (aspectRatio > 0) {
                this.aspectRatio = aspectRatio
                requestLayout()
            }
        }

        override fun onMeasure(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int,
        ) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = (width / aspectRatio).toInt()
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
