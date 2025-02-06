package com.constant.japaneseeveryday.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.tabs.TabLayout

class HHTabLayout
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : TabLayout(context, attrs) {
        private var enableTabs: Boolean = true

        init {
            init()
        }

        private fun init() {
            enableTabs = true
        }

        override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
            return !enableTabs
        }

        fun setEnableTabs(enable: Boolean) {
            this.enableTabs = enable
        }
    }
