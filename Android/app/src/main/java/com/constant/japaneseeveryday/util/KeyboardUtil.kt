package com.constant.japaneseeveryday.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

object KeyboardUtil {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun hideKeyboard(
        context: Context,
        view: View,
    ) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
// Private Method
}
