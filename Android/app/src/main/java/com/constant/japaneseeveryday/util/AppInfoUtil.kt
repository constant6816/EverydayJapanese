package com.constant.japaneseeveryday.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

object AppInfoUtil {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    // Override Method or Basic Method

    // Public Method
    // app id 가져오기
    fun getAppId(context: Context): String {
        var appId = ""
        try {
            val pm: PackageManager = context.getPackageManager()
            val i: PackageInfo = pm.getPackageInfo(context.getPackageName(), 0)
            appId = i.applicationInfo.loadDescription(pm).toString() + ""
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return appId
    }

    // app name 가져오기
    fun getAppName(context: Context): String {
        var appName = ""
        try {
            val pm: PackageManager = context.getPackageManager()
            val i: PackageInfo = pm.getPackageInfo(context.getPackageName(), 0)
            appName = i.applicationInfo.loadLabel(pm).toString() + ""
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return appName
    }

    // package name 가져오기
    fun getPackageName(context: Context): String {
        var packageName = "" // 패키지명 예시 데이터
        try {
            val packagemanager: PackageManager = context.getPackageManager()
            val appinfo =
                packagemanager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            packageName = packagemanager.getApplicationLabel(appinfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return packageName
    }

    // app version 가져오기
    fun getVersion(context: Context): String {
        var versionName = ""
        try {
            val pInfo: PackageInfo =
                context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
            versionName = pInfo.versionName + ""
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    // app version code 가져오기
    fun getVersionCode(context: Context): Int {
        var versionCode = 1
        try {
            val i: PackageInfo =
                context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
            versionCode = i.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return versionCode
    }
    // Private Method
}
