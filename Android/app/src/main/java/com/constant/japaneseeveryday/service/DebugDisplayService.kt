package com.constant.japaneseeveryday.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication
import com.constant.japaneseeveryday.singleton.DebugVariable
import com.constant.japaneseeveryday.singleton.Pref
import com.constant.japaneseeveryday.singleton.PrefManager
import com.constant.japaneseeveryday.util.AppInfoUtil
import com.constant.japaneseeveryday.util.HHIntent
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import com.constant.japaneseeveryday.view.DebugStatusView

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class DebugDisplayService : Service() {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        // 설정 - 애플리케이션 정보 - 앱 이름 - 다른 앱 위에 표시 - ON 이 되어 있는지
        fun canDrawOverlays(context: Context): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                Settings.canDrawOverlays(context)
        }
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    private var windowManager: WindowManager? = null
    private var debugStatusView: DebugStatusView? = null
    private var debugWindowReceiver: BroadcastReceiver? = null
    private var isDebugWindow = false

    // Private Variable
    // ----------------------------------------------------
    // Override Method or Basic Method
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
        HHLog.d(TAG, "onBind")
    }

    override fun onCreate() {
        super.onCreate()
        HHLog.d(TAG, "onCreate")
        debugWindowReceiver =
            object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context?,
                    intent: Intent?,
                ) {
                    if (intent == null) {
                        return
                    }
                    when (intent.action) {
                        HHIntent.ACTION_DEBUG_WINDOW_SHOW -> {
                            createViews()
                            if (debugStatusView == null) {
                                return
                            }
                            HHLog.d(TAG, "View.VISIBLE")
                            debugStatusView!!.visibility = View.VISIBLE
                        }
                        HHIntent.ACTION_DEBUG_WINDOW_HIDE -> {
                            if (debugStatusView == null) {
                                return
                            }
                            HHLog.d(TAG, "View.GONE")
                            debugStatusView!!.visibility = View.GONE
                        }
                        HHIntent.ACTION_DEBUG_WINDOW_SET_TEXT -> {
                            if (debugStatusView == null) {
                                return
                            }
                            debugStatusView!!.debugText = nonNull(intent.getStringExtra(HHIntent.EXTRA_DEBUG_WINDOW_TEXT))
                            debugStatusView!!.invalidate()
                        }
                    }
                }
            }
        var filter =
            IntentFilter().apply {
                addAction(HHIntent.ACTION_DEBUG_WINDOW_SHOW)
                addAction(HHIntent.ACTION_DEBUG_WINDOW_HIDE)
                addAction(HHIntent.ACTION_DEBUG_WINDOW_SET_TEXT)
            }
        registerReceiver(debugWindowReceiver, filter, RECEIVER_NOT_EXPORTED)

        createViews()
        showVersionInfo()
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        HHLog.d(TAG, "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(debugWindowReceiver)
        HHLog.d(TAG, "onDestroy")
    }

    // Public Method
    fun createViews() {
        if (isDebugWindow == true) {
            return
        }

        if (!canDrawOverlays(baseContext)) {
            return
        }
        HHLog.d(TAG, "createViews")

        windowManager = baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        debugStatusView = DebugStatusView(this)
        debugStatusView!!.isClickable = false
        debugStatusView!!.isFocusable = false
        var params =
            WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT,
            )
        params.gravity = Gravity.CENTER or Gravity.TOP
        params.setTitle("DebugStatusView")

        windowManager!!.addView(debugStatusView, params)

        if (DebugVariable.getInstance().isShowDebugText &&
            (JapaneseEverydayApplication.context.applicationContext as JapaneseEverydayApplication).appLifecycleTracker!!.numStartedActivity >= 1
        ) {
            debugStatusView!!.setVisibility(View.VISIBLE)
        } else {
            debugStatusView!!.setVisibility(View.GONE)
        }
        isDebugWindow = true
    }

    // Private Method
    private fun showVersionInfo() {
        var intent = Intent()
        val server = PrefManager.getInstance().getStringValue(Pref.server.name)
        val version = AppInfoUtil.getVersion(JapaneseEverydayApplication.context)
        val versionCode = AppInfoUtil.getVersionCode(JapaneseEverydayApplication.context)
        val strText = String.format("%s %s(%s)", server, version, versionCode)
        intent.action = HHIntent.ACTION_DEBUG_WINDOW_SET_TEXT
        intent.putExtra(HHIntent.EXTRA_DEBUG_WINDOW_TEXT, strText)
        sendBroadcast(intent)
    }
}
