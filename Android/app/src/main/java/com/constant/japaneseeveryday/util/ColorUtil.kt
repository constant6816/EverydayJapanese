package com.constant.japaneseeveryday.util

import android.content.Context
import android.content.res.Configuration

object ColorUtil {
    fun isDarkMode(context: Context,) : Boolean {
        val isDarkMode: Boolean = (context.getResources().getConfiguration().uiMode
                and Configuration.UI_MODE_NIGHT_MASK) === Configuration.UI_MODE_NIGHT_YES
        return isDarkMode
    }
}