package com.constant.japaneseeveryday.model

import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication
import com.constant.japaneseeveryday.singleton.Pref
import com.constant.japaneseeveryday.singleton.PrefManager
import com.constant.japaneseeveryday.util.AppInfoUtil
import com.constant.japaneseeveryday.util.ServerEnum

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class Configuration private constructor() {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: Configuration

        fun getInstance(): Configuration {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = Configuration()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant

    // Public Variable
    public var urlProduction = "http://XXXXXXXXXXXXXXX"
    public var urlStage = "http://XXXXXXXXXXXXXXX"
    public var urlDevelop = "http://XXXXXXXXXXXXXXX"
    public var urlHome = "http://192.168.0.8:8080"
    public var urlOffice = "http://169.254.113.215:8080"

    public var androidReviewVersion = "1.01.02"
    public var isAndroidReviewing = false

    // Private Variable
    // Override Method or Basic Method
    // Public Method
    fun getBaseUrl(): String {
        when (PrefManager.getInstance().getStringValue(Pref.server.name)) {
            ServerEnum.production.name -> {
                if (isAndroidReviewing && androidReviewVersion ==
                    AppInfoUtil.getVersion(
                        JapaneseEverydayApplication.context,
                    )
                ) {
                    return urlStage
                } else {
                    return urlProduction
                }
            }
            ServerEnum.stage.name -> {
                return urlStage
            }
            ServerEnum.develop.name -> {
                return urlDevelop
            }
            ServerEnum.home.name -> {
                return urlHome
            }
            ServerEnum.office.name -> {
                return urlOffice
            }
            else -> {
                return "ERROR"
            }
        }
    }
    // Private Method
}
