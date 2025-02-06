package com.constant.japaneseeveryday.util

import android.content.Context
import android.os.Build
import android.provider.Settings

object DeviceInfoUtil {
    /**
     * device id 가져오기
     * @param context
     * @return
     */
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * device 제조사 가져오기
     * @return
     */
    val manufacturer: String
        get() = Build.MANUFACTURER

    /**
     * device 브랜드 가져오기
     * @return
     */
    val deviceBrand: String
        get() = Build.BRAND

    /**
     * device 모델명 가져오기
     * @return
     */
    val deviceModel: String
        get() = Build.MODEL

    /**
     * device Android OS 버전 가져오기
     * @return
     */
    val deviceOs: String
        get() = Build.VERSION.RELEASE

    /**
     * device SDK 버전 가져오기
     * @return
     */
    val deviceSdk: Int
        get() = Build.VERSION.SDK_INT
}
