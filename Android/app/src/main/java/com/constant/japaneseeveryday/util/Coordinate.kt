package com.constant.japaneseeveryday.util

import android.content.res.Resources
import java.lang.Integer.max
import java.lang.Integer.min

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
object Coordinate {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun getWidth(): Int {
        val displayMetrics = Resources.getSystem().getDisplayMetrics()
        return displayMetrics.widthPixels
    }

    fun getHeight(): Int {
        val displayMetrics = Resources.getSystem().getDisplayMetrics()
        return displayMetrics.heightPixels
    }

    fun getShortAxis(): Int {
        val displayMetrics = Resources.getSystem().getDisplayMetrics()
        return min(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    fun getLongAxis(): Int {
        val displayMetrics = Resources.getSystem().getDisplayMetrics()
        return max(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }
    // Private Method
}
