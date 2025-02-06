package com.constant.japaneseeveryday.util

import android.content.res.Resources

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

object DisplayUtil {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun pxToDp(px: Float): Float {
        return px / Resources.getSystem().displayMetrics.density
    }

    fun dpToPx(dp: Float): Float {
        return dp * Resources.getSystem().displayMetrics.density
    }
    // Private Method
}
