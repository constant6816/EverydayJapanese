package com.constant.japaneseeveryday.scene.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.constant.japaneseeveryday.databinding.ActivitySplashBinding
import com.constant.japaneseeveryday.scene.main.MainActivity
import com.constant.japaneseeveryday.service.DebugDisplayService
import com.constant.japaneseeveryday.service.FcmService
import com.constant.japaneseeveryday.singleton.AccountManager
import com.constant.japaneseeveryday.singleton.Pref
import com.constant.japaneseeveryday.singleton.PrefManager
import com.constant.japaneseeveryday.util.FeatureConst
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import io.reactivex.rxjava3.disposables.CompositeDisposable

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class SplashActivity : AppCompatActivity() {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    // Public Variable

    // ----------------------------------------------------
    // Private Variable
    private lateinit var binding: ActivitySplashBinding

    private val compositeDisposable = CompositeDisposable()

    // ----------------------------------------------------
    // Override Method or Basic Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HHLog.d(TAG, "SplashActivity, onCreate()")
        initializeVariables()
        initializeViews()

        // 서비스는 무조건 시작하자.
        val intent = Intent(this, DebugDisplayService::class.java)
        startService(intent)

        val fcm = Intent(this, FcmService::class.java)
        startService(fcm)
    }

    // Public Method
    // Private Method
    private fun initializeVariables() {
    }

    private fun initializeViews() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
        }
        Handler(Looper.myLooper()!!).postDelayed({
            if (AccountManager.getInstance().isLogin()) {
                AccountManager.getInstance().processLoginWithScreen(this@SplashActivity)
            } else {
                val intent: Intent
                intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)
    }
}
