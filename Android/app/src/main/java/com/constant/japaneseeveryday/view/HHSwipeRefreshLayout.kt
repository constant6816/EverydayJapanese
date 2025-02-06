package com.constant.japaneseeveryday.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

// SwipeRefreshLayout 를 사용하면 RecyclerView에서 터치가 되지 않아 상속 받아 재정의함.
class HHSwipeRefreshLayout : SwipeRefreshLayout {
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs,
    )

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return false
    }
}
