package com.constant.japaneseeveryday.util

import android.util.Log
import com.constant.japaneseeveryday.singleton.DebugVariable
import com.google.firebase.crashlytics.FirebaseCrashlytics

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

object HHLog {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun e(
        tag: String,
        logText: String,
    ) {
        // if (DebugVariable.getInstance().isLogEnable) {
        Log.d(tag, logText)
        // }

        val userInfo = mutableMapOf<String, Any>()
        userInfo["Error"] = "[E] $logText"
        val error = Exception("[HHLog] $userInfo")
        FirebaseCrashlytics.getInstance().recordException(error)
    }

    fun w(
        tag: String,
        logText: String,
    ) {
        if (DebugVariable.getInstance().isLogEnable) {
            Log.d(tag, logText)
        }
    }

    fun d(
        tag: String,
        logText: String,
    ) {
        if (DebugVariable.getInstance().isLogEnable) {
            Log.d(tag, logText)
        }
    }

    fun v(
        tag: String,
        logText: String,
    ) {
        if (DebugVariable.getInstance().isLogEnable) {
            Log.d(tag, logText)
        }
    }
    // Private Method
}
