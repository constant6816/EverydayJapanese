package com.constant.japaneseeveryday.singleton

import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class PushAlarmManager private constructor() {
    // Public Inner Class, Struct, Enum, Interface
    enum class PushAlarmType {
        neccessary, // 필수 알림
        basic, // 기본 알림
        marketing, // 마케팅 알림
    }

    // companion object
    companion object {
        @Volatile
        private lateinit var instance: PushAlarmManager

        fun getInstance(): PushAlarmManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = PushAlarmManager()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun subscribe(pushAlarmType: PushAlarmType) {
        val serverType = PrefManager.getInstance().getStringValue(Pref.server.name)
        val topic = String.format("attendance_%s_%s", serverType, pushAlarmType.name)
        HHLog.d(TAG, "subscribe($topic)")
        Firebase.messaging.subscribeToTopic(topic)
    }

    fun unsubscribe(pushAlarmType: PushAlarmType) {
        val serverType = PrefManager.getInstance().getStringValue(Pref.server.name)
        val topic = String.format("attendance_%s_%s", serverType, pushAlarmType.name)
        HHLog.d(TAG, "unsubscribe($topic)")
        Firebase.messaging.unsubscribeFromTopic(topic)
    }
    // Private Method
}
