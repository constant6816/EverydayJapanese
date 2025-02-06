package com.constant.japaneseeveryday.singleton

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday._changes.Model
import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication
import com.constant.japaneseeveryday.model.Configuration
import com.constant.japaneseeveryday.scene.common.HHDialog
import com.constant.japaneseeveryday.scene.login.SplashActivity
import com.constant.japaneseeveryday.util.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class BootingManager {
    // Public Inner Class, Struct, Enum, Interface
    enum class PopupType {
        // 긴급 공지
        emergencyAnnounce,

        // 필수 업데이트
        necessaryUpdate,

        // 일반 업데이트
        generalUpdate,
    }

    inner class Popup {
        var popupType: PopupType
        var popupContent: String?

        constructor(popupType: PopupType, popupContent: String?) {
            this.popupType = popupType
            this.popupContent = popupContent
        }
    }

    class HHRemoteConfig {
        var androidReviewVersion: String
        var isAndroidReviewing: Boolean
        var isUnderMaintenaceProduction: Boolean
        var isUnderMaintenaceStage: Boolean
        var isUnderMaintenaceDevelop: Boolean
        var noticeMessageProduction: String
        var noticeMessageStage: String
        var noticeMessageDevelop: String
        var serverUrlProduction: String
        var serverUrlStage: String
        var serverUrlDevelop: String

        constructor(
            androidReviewVersion: String,
            isAndroidReviewing: Boolean,
            isUnderMaintenaceProduction: Boolean,
            isUnderMaintenaceStage: Boolean,
            isUnderMaintenaceDevelop: Boolean,
            noticeMessageProduction: String,
            noticeMessageStage: String,
            noticeMessageDevelop: String,
            serverUrlProduction: String,
            serverUrlStage: String,
            serverUrlDevelop: String,
        ) {
            this.androidReviewVersion = androidReviewVersion
            this.isAndroidReviewing = isAndroidReviewing
            this.isUnderMaintenaceProduction = isUnderMaintenaceProduction
            this.isUnderMaintenaceStage = isUnderMaintenaceStage
            this.isUnderMaintenaceDevelop = isUnderMaintenaceDevelop
            this.noticeMessageProduction = noticeMessageProduction
            this.noticeMessageStage = noticeMessageStage
            this.noticeMessageDevelop = noticeMessageDevelop
            this.serverUrlProduction = serverUrlProduction
            this.serverUrlStage = serverUrlStage
            this.serverUrlDevelop = serverUrlDevelop
        }
    }

    // ----------------------------------------------------
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: BootingManager

        fun getInstance(): BootingManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = BootingManager()
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    public var bootCompletedSubject: ReplaySubject<Boolean>
    public var isUnderMaintenace: Boolean = false

    // Private Variable
    private var popupSubject: ReplaySubject<ArrayList<Popup>>
    private var firstFlag = true
    private val compositeDisposable = CompositeDisposable()

    // Override Method or Basic Method
    constructor() {
        bootCompletedSubject = ReplaySubject.createWithSize(1)
        popupSubject = ReplaySubject.createWithSize(1)
    }

    // Public Method
    fun start(context: Context) {
        bootCompletedSubject = ReplaySubject.createWithSize(1)
        getFcmToken()
            .flatMap {
                fetchRemoteConfig()
            }
            .flatMap { remoteConfig ->
                HHLog.d(TAG, "androidReviewVersion = ${remoteConfig.androidReviewVersion}")
                HHLog.d(TAG, "isAndroidReviewing = ${remoteConfig.isAndroidReviewing}")
                HHLog.d(TAG, "isUnderMaintenaceProduction = ${remoteConfig.isUnderMaintenaceProduction}")
                HHLog.d(TAG, "isUnderMaintenaceStage = ${remoteConfig.isUnderMaintenaceStage}")
                HHLog.d(TAG, "isUnderMaintenaceDevelop = ${remoteConfig.isUnderMaintenaceDevelop}")
                HHLog.d(TAG, "noticeMessageProduction = ${remoteConfig.noticeMessageProduction}")
                HHLog.d(TAG, "noticeMessageStage = ${remoteConfig.noticeMessageStage}")
                HHLog.d(TAG, "noticeMessageDevelop = ${remoteConfig.noticeMessageDevelop}")
                HHLog.d(TAG, "serverUrlProduction = ${remoteConfig.serverUrlProduction}")
                HHLog.d(TAG, "serverUrlStage = ${remoteConfig.serverUrlStage}")
                HHLog.d(TAG, "serverUrlDevelop = ${remoteConfig.serverUrlDevelop}")

                Configuration.getInstance().urlProduction = remoteConfig.serverUrlProduction
                Configuration.getInstance().urlStage = remoteConfig.serverUrlStage
                Configuration.getInstance().urlDevelop = remoteConfig.serverUrlDevelop
                Configuration.getInstance().isAndroidReviewing = remoteConfig.isAndroidReviewing
                Configuration.getInstance().androidReviewVersion = remoteConfig.androidReviewVersion

                if (PrefManager.getInstance().getStringValue(Pref.server.name) == ServerEnum.production.name) {
                    isUnderMaintenace = remoteConfig.isUnderMaintenaceProduction
                } else if (PrefManager.getInstance().getStringValue(Pref.server.name) == ServerEnum.stage.name) {
                    isUnderMaintenace = remoteConfig.isUnderMaintenaceStage
                } else { // develop
                    isUnderMaintenace = remoteConfig.isUnderMaintenaceDevelop
                }
                if (isUnderMaintenace) {
                    val popups = ArrayList<Popup>()
                    if (PrefManager.getInstance().getStringValue(Pref.server.name) == ServerEnum.production.name) {
                        popups.add(Popup(PopupType.emergencyAnnounce, remoteConfig.noticeMessageProduction))
                    } else if (PrefManager.getInstance().getStringValue(Pref.server.name) == ServerEnum.stage.name) {
                        popups.add(Popup(PopupType.emergencyAnnounce, remoteConfig.noticeMessageStage))
                    } else { // develop
                        popups.add(Popup(PopupType.emergencyAnnounce, remoteConfig.noticeMessageDevelop))
                    }
                    return@flatMap Observable.just(popups)
                } else {
                    return@flatMap GlobalVariable.getInstance().noStyleRepository.config(context).map { configModel ->
                        HHLog.d(TAG, "configModel = $configModel")
                        val popups = ArrayList<Popup>()
                        EntityVariable.getInstance().configModel = configModel
                        // if configModel.versionIos != UserDefaultManager.shared.version!.replacingOccurrences(of: "-debug", with: "") {
                        val marketVersion = Version(configModel.marketVersion)
                        val curVersion = Version(AppInfoUtil.getVersion(JapaneseEverydayApplication.context))
                        HHLog.d(TAG, "marketVersion = $marketVersion")
                        HHLog.d(TAG, "curVersion = $curVersion")
                        if (configModel.isRequiredUpdate) {
                            popups.add(Popup(PopupType.necessaryUpdate, null))
                        } else {
                            if (marketVersion.isGreaterThan(curVersion)) {
                                popups.add(Popup(PopupType.generalUpdate, null))
                            }
                        }
                        popups
                    }
                }
            }
            .subscribe({ popups ->
                HHLog.d(TAG, "popupSubject.onNext()")
                popupSubject.onNext(popups)
                if (popups.isEmpty()) {
                    HHLog.d(TAG, "bootCompletedSubject.onNext()")
                    bootCompletedSubject.onNext(true)
                }
            }, {
                HHLog.e(TAG, "BootingManager.start() error")
                HHLog.d(TAG, "popupSubject.onNext()")
                popupSubject.onNext(ArrayList<Popup>())
                // bootCompletedSubject.onNext(true)
            }).let { compositeDisposable.add(it) }
    }

    fun resume(context: Context) {
        // HHLog.e(TAG, "resume() now = ${System.currentTimeMillis()}, pingTime = ${GlobalVariable.getInstance().pingTime}")
        // 4시간이 지났다면,
        if (System.currentTimeMillis() - GlobalVariable.getInstance().pingTime > 3600 * 4 * 1000) {
            HHLog.e(TAG, "resume() now = ${System.currentTimeMillis()}, pingTime = ${GlobalVariable.getInstance().pingTime}")
            HHLog.e(TAG, "resume() restart ~~~")
            start(context)
            GlobalVariable.getInstance().pingTime = System.currentTimeMillis()
            navigateToSplashScreen(context)
        }
    }

    // 해당 함수는 첫화면인 곳은 모든 곳에서 호출해 줘야 하고,
    // 그중에서 한곳에서만 수행이 되어야 한다.
    fun process(context: Context) {
        if (firstFlag) {
//            LoadingView.shared.show(tag:String(describing: self))
            popupSubject
                .subscribe({ popups ->
                    showPopupUntilExhausted(context, popups)
                }, { error ->
                    HHLog.e(TAG, "error = $error")
                }).let { compositeDisposable.add(it) }
            firstFlag = false
        }
    }

    // Private Method
    // 보여져야 할 팝업이 없을때까지 팝업을 보여줘야 한다.
    private fun showPopupUntilExhausted(
        context: Context,
        popups: ArrayList<Popup>,
    ) {
        HHLog.d(TAG, "showPopupUntilExhausted() popups = $popups")
        if (!popups.isEmpty()) {
            val popup = popups.first()
            when (popup.popupType) {
                PopupType.emergencyAnnounce -> {
                    HHDialog(
                        context,
                        context.getString(
                            R.string.notices,
                        ),
                        popup.popupContent,
                        context.getString(R.string.common_quit_the_app),
                        { _, _ ->
                            ActivityCompat.finishAffinity(context as Activity)
                            System.exit(0)
                        },
                    )
                }
                PopupType.necessaryUpdate -> {
                    HHDialog(
                        context,
                        context.getString(
                            R.string.required_updates,
                        ),
                        String.format(context.getString(R.string.update_enjoy_new), context.getString(R.string.app_name)),
                        context.getString(R.string.update),
                        { _, _ ->
                            // 사이트 이동
                            val url = String.format("https://play.google.com/store/apps/details?id=%s", Model.packageName)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                            // 앱 종료
                            ActivityCompat.finishAffinity(context as Activity)
                            System.exit(0)
                        },
                    )
                }
                PopupType.generalUpdate -> {
                    bootCompletedSubject.onNext(true)
                    HHDialog(
                        context,
                        context.getString(
                            R.string.update,
                        ),
                        String.format(context.getString(R.string.update_enjoy_new), context.getString(R.string.app_name)),
                        context.getString(R.string.update),
                        { _, _ ->
                            // 사이트 이동
                            val url = String.format("https://play.google.com/store/apps/details?id=%s", Model.packageName)
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        context.getString(R.string.common_later),
                    )
                }
            }
            val resultPopups = popups
            resultPopups.removeFirst()
            showPopupUntilExhausted(context, resultPopups)
        } else {
            HHLog.d(TAG, "bootCompletedSubject.onNext()")
            bootCompletedSubject.onNext(true)
        }
    }

    private fun fetchRemoteConfig(): Observable<HHRemoteConfig> {
        return Observable.create { emitter ->
            val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
            val configSettings =
                remoteConfigSettings {
                    // 배포될때에는 10분에 한번만 fetch 하도록 수정예정
                    minimumFetchIntervalInSeconds = 600
                    // minimumFetchIntervalInSeconds = 0
                }
            remoteConfig.setConfigSettingsAsync(configSettings)
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

            remoteConfig.fetchAndActivate()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        HHLog.d(TAG, "remoteConfig is successful")
//                        Toast.makeText(this, "Fetch and activate succeeded",
//                            Toast.LENGTH_SHORT).show()
                    } else {
                        HHLog.d(TAG, "remoteConfig is unsuccessful")
//                        Toast.makeText(this, "Fetch failed",
//                            Toast.LENGTH_SHORT).show()
                    }
                    val androidReviewVersion = remoteConfig.getString("android_review_version")
                    val isAndroidReviewing = remoteConfig.getBoolean("android_reviewing")
                    val isUnderMaintenaceProduction = remoteConfig.getBoolean("server_under_maintenance_production")
                    val isUnderMaintenaceStage = remoteConfig.getBoolean("server_under_maintenance_stage")
                    val isUnderMaintenaceDevelop = remoteConfig.getBoolean("server_under_maintenance_develop")
                    val noticeMessageProduction = remoteConfig.getString("server_notice_message_production")
                    val noticeMessageStage = remoteConfig.getString("server_notice_message_stage")
                    val noticeMessageDevelop = remoteConfig.getString("server_notice_message_develop")
                    val serverUrlProduction = remoteConfig.getString("server_url_production")
                    val serverUrlStage = remoteConfig.getString("server_url_stage")
                    val serverUrlDevelop = remoteConfig.getString("server_url_develop")
                    emitter.onNext(
                        HHRemoteConfig(
                            androidReviewVersion,
                            isAndroidReviewing,
                            isUnderMaintenaceProduction,
                            isUnderMaintenaceStage,
                            isUnderMaintenaceDevelop,
                            noticeMessageProduction,
                            noticeMessageStage,
                            noticeMessageDevelop,
                            serverUrlProduction,
                            serverUrlStage,
                            serverUrlDevelop,
                        ),
                    )
                    emitter.onComplete()
                }
        }
    }

    private fun getFcmToken(): Observable<Boolean> {
        return Observable.create { emitter ->
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    HHLog.d(TAG, "FCM TOKEN = ${task.result}")
                    PrefManager.getInstance().setValue(Pref.fcmToken.name, task.result)
                    emitter.onNext(true)
                    emitter.onComplete()
                }
            }
        }
    }

    private fun navigateToSplashScreen(context: Context) {
        val intent =
            Intent(context, SplashActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
        context.startActivity(intent)
    }
}
