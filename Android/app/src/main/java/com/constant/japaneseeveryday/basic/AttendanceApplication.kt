package com.constant.japaneseeveryday.basic

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.singleton.BootingManager
import com.constant.japaneseeveryday.singleton.DebugVariable
import com.constant.japaneseeveryday.singleton.GlobalVariable
import com.constant.japaneseeveryday.singleton.Pref
import com.constant.japaneseeveryday.singleton.PrefManager
import com.constant.japaneseeveryday.util.FeatureConst
import com.constant.japaneseeveryday.util.HHIntent
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class JapaneseEverydayApplication : Application() {
    // Public Inner Class, Struct, Enum, Interface
    class AppLifecycleTracker(private val context: Context) : Application.ActivityLifecycleCallbacks {
        private val TAG = nonNull(this::class.simpleName)
        public var numStartedActivity = 0

        override fun onActivityStarted(activity: Activity) {
            HHLog.d(TAG, "onActivityStarted")
            numStartedActivity++
            // app went to foreground
            if (DebugVariable.getInstance().isShowDebugText) {
                val intent = Intent()
                intent.action = HHIntent.ACTION_DEBUG_WINDOW_SHOW
                context.sendBroadcast(intent)
            }
            BootingManager.getInstance().resume(context)
        }

        override fun onActivityStopped(activity: Activity) {
            HHLog.d(TAG, "onActivityStopped")
            numStartedActivity--
            if (numStartedActivity == 0) {
                // app went to background
                val intent = Intent()
                intent.action = HHIntent.ACTION_DEBUG_WINDOW_HIDE
                context.sendBroadcast(intent)
            }
        }

        override fun onActivityCreated(
            p0: Activity,
            p1: Bundle?,
        ) {
            // TODO("Not yet implemented")
        }

        override fun onActivityResumed(p0: Activity) {
            // TODO("Not yet implemented")
        }

        override fun onActivityPaused(p0: Activity) {
            // TODO("Not yet implemented")
        }

        override fun onActivitySaveInstanceState(
            p0: Activity,
            p1: Bundle,
        ) {
            // TODO("Not yet implemented")
        }

        override fun onActivityDestroyed(p0: Activity) {
            // TODO("Not yet implemented")
        }
    }

//    class AppLifecycleObserver(private val context: Context) : DefaultLifecycleObserver {
//        override fun onStop(owner: LifecycleOwner) {
//            // 앱이 백그라운드로 전환될 때 시간 저장
//        }
//
//        override fun onStart(owner: LifecycleOwner) {
//            // 앱이 포그라운드로 돌아올 때 시간 확인
//            BootingManager.getInstance().resume(context)
//        }
//    }

    // companion object
    companion object {
        lateinit var context: Context
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    var appLifecycleTracker: AppLifecycleTracker? = null

    // Private Variable
    // Override Method or Basic Method
    override fun onCreate() {
        super.onCreate()
        context = this
        FeatureConst.initialize(context)

        initializePreference()

        GlobalVariable.getInstance().startTime = System.currentTimeMillis()
        GlobalVariable.getInstance().pingTime = System.currentTimeMillis()

        HHLog.d(TAG, "[TIME] App Start : ${System.currentTimeMillis() - GlobalVariable.getInstance().startTime}")
        HHLog.d(TAG, "AttendanceApplication.onCreate()")

        appLifecycleTracker = AppLifecycleTracker(this)
        registerActivityLifecycleCallbacks(appLifecycleTracker)

        //ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(this))
        HHLog.d(TAG, "[TIME] App onCreate()Finished : ${System.currentTimeMillis() - GlobalVariable.getInstance().startTime}")

        initialize3rdPartyLogin()
        initializeRxJava()
        BootingManager.getInstance().start(this@JapaneseEverydayApplication)
    }

    // Public Method
    fun isActivityVisible(): Boolean {
        return appLifecycleTracker!!.numStartedActivity != 0
    }

    // Private Method
    private fun initializePreference() {
        PrefManager.getInstance().registerPreference(context)
        DebugVariable.getInstance().isLogEnable = PrefManager.getInstance().getBooleanValue(Pref.logEnable.name)
        DebugVariable.getInstance().isShowDebugText = PrefManager.getInstance().getBooleanValue(Pref.showDebugText.name)
        PrefManager.getInstance().printPreference()
    }

    private fun initialize3rdPartyLogin() {
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        NaverIdLoginSDK.initialize(
            this,
            getString(R.string.naver_client_id),
            getString(R.string.naver_client_secret),
            getString(R.string.app_name),
        )
    }

    private fun initializeRxJava() {
        RxJavaPlugins.setErrorHandler { e ->
            var error = e
            if (error is UndeliverableException) {
                error = e.cause!!
            }
            if (error is IOException || error is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (error is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (error is NullPointerException || error is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            if (error is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), error)
                return@setErrorHandler
            }
            HHLog.w(TAG, "Undeliverable exception received, not sure what to do")
        }
    }
}
