package com.constant.japaneseeveryday.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull

// 디버깅 용으로만 사용
class FCMReceiver : BroadcastReceiver() {
    private val TAG = nonNull(this::class.simpleName)

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        intent?.getExtras().let { bundle ->
            if (bundle != null) {
                for (key in bundle.keySet()) {
                    val value: Any? = bundle.get(key)
                    HHLog.d(TAG, "Key: $key Value: $value")
                }
            }
        }
    }
}
