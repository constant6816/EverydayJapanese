package com.constant.japaneseeveryday.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class BarButtonView : FrameLayout {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // ----------------------------------------------------
    // Private Variable
    lateinit var textview1: TextView
    lateinit var textview2: TextView

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

    // Private Method
    private fun initializeViews() {
        HHLog.d(TAG, "initializeViews")
        if (context == null) {
            return
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        inflater?.inflate(R.layout.view_barbutton, this, true)

        textview1 = findViewById(R.id.textview_1)
        textview2 = findViewById(R.id.textview_2)
    }
}
