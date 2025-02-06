package com.constant.japaneseeveryday.singleton

import android.content.Context
import com.constant.japaneseeveryday.BuildConfig
import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.ServerEnum
import com.constant.japaneseeveryday.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
enum class Pref {
    // 로그인 관련 ------------
    loginType,
    oldLoginType,
    accessToken3rd,
    refreshToken3rd,
    userEmail,
    userName,
    userId,
    accessToken,
    refreshToken,
    // 로그인 관련 ------------

    // 버젼
    version,
    buildNo,

    deviceToken,
    fcmToken,

    // 디버깅 관련
    server,
    logEnable,
    showDebugText,
    sample,

    // 일반
    donotDisplayNotificationPermission,
}

class PrefManager {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: PrefManager

        fun getInstance(): PrefManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = PrefManager()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    private val PREFERENCE = "com.constant.attendance.sharepreference"

    // Public Variable

    // Private Variable
    private val TAG = nonNull(this::class.simpleName)
    private var defaultValueDic = HashMap<String, Any>()
    private var stringKeys = ArrayList<String>()
    private var intKeys = ArrayList<String>()
    private var booleanKeys = ArrayList<String>()

    // Override Method or Basic Method
    // int , boolean 은 무조건 default 값을 넣어야 한다.
    fun registerPreference(context: Context) {
        // default 값
        defaultValueDic.put(Pref.userId.name, 0)
        if (BuildConfig.IS_DEBUG) {
            // if (true) {
            defaultValueDic.put(Pref.server.name, ServerEnum.develop.name)
            defaultValueDic.put(Pref.logEnable.name, true)
            defaultValueDic.put(Pref.showDebugText.name, true)
            defaultValueDic.put(Pref.sample.name, true)
        } else {
            defaultValueDic.put(Pref.server.name, ServerEnum.production.name)
            defaultValueDic.put(Pref.logEnable.name, false)
            defaultValueDic.put(Pref.showDebugText.name, false)
            defaultValueDic.put(Pref.sample.name, false)
        }

        defaultValueDic.put(Pref.donotDisplayNotificationPermission.name, false)
//        defaultValueDic.put(Pref.accessToken3rd.name, null)

        // key 입력
        stringKeys.add(Pref.loginType.name)
        stringKeys.add(Pref.oldLoginType.name)
        stringKeys.add(Pref.accessToken3rd.name)
        stringKeys.add(Pref.refreshToken3rd.name)
        stringKeys.add(Pref.userEmail.name)
        stringKeys.add(Pref.userName.name)
        stringKeys.add(Pref.accessToken.name)
        stringKeys.add(Pref.refreshToken.name)
        stringKeys.add(Pref.version.name)
        stringKeys.add(Pref.buildNo.name)
        stringKeys.add(Pref.deviceToken.name)
        stringKeys.add(Pref.fcmToken.name)
        stringKeys.add(Pref.server.name)

        // int
        intKeys.add(Pref.userId.name)

        // boolean
        booleanKeys.add(Pref.logEnable.name)
        booleanKeys.add(Pref.showDebugText.name)
        booleanKeys.add(Pref.sample.name)

        booleanKeys.add(Pref.donotDisplayNotificationPermission.name)
    }

    // Public Method
    fun getStringValue(key: String): String? {
        return JapaneseEverydayApplication.context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            .getString(key, defaultValueDic.get(key) as? String)
    }

    fun getIntValue(key: String): Int {
        return JapaneseEverydayApplication.context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            .getInt(key, defaultValueDic.get(key) as Int)
    }

    fun getBooleanValue(key: String): Boolean {
        return JapaneseEverydayApplication.context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
            .getBoolean(key, defaultValueDic.get(key) as Boolean)
    }

    fun setValue(
        key: String,
        value: String?,
    ) {
        JapaneseEverydayApplication.context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit()
            .putString(key, value).apply()
    }

    fun setValue(
        key: String,
        value: Int,
    ) {
        JapaneseEverydayApplication.context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit()
            .putInt(key, value).apply()
    }

    fun setValue(
        key: String,
        value: Boolean,
    ) {
        JapaneseEverydayApplication.context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit()
            .putBoolean(key, value).apply()
    }

    fun printPreference() {
        HHLog.d(TAG, "-----------------------------------------------")
        HHLog.d(TAG, "==============UserDefault==============")
        for (key in stringKeys) {
            HHLog.d(TAG, "$key = ${getStringValue(key)}")
        }
        for (key in intKeys) {
            HHLog.d(TAG, "$key = ${getIntValue(key)}")
        }
        for (key in booleanKeys) {
            HHLog.d(TAG, "$key = ${getBooleanValue(key)}")
        }

        HHLog.d(TAG, "-----------------------------------------------")
    }
    // Private Method
}
