package com.constant.japaneseeveryday.util

import android.app.ActivityManager
import android.content.Context

object ActivityUtil {
    fun isActivityForeground(
        context: Context,
        activityClass: Class<*>,
    ): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (activityManager != null) {
            if (activityManager.getRunningTasks(1).size > 0) {
                val foregroundActivity =
                    activityManager.getRunningTasks(1)[0].topActivity?.className
                return foregroundActivity == activityClass.name
            }
        }
        return false
    }
}
