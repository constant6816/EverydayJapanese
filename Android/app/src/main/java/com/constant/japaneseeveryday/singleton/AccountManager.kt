package com.constant.japaneseeveryday.singleton

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.constant.japaneseeveryday.scene.main.MainActivity
import com.constant.japaneseeveryday.util.HHError
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import io.reactivex.rxjava3.disposables.CompositeDisposable

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
enum class LoginTypeEnum(val value: String) {
    NONE("NONE"),
    GOOGLE("GOOGLE"),
    KAKAO("KAKAO"),
    NAVER("NAVER"),
    ;

    companion object {
        val rawToEnum =
            mapOf(
                NONE.value to NONE,
                GOOGLE.value to GOOGLE,
                KAKAO.value to KAKAO,
                NAVER.value to NAVER,
            )

        fun ofRaw(raw: String): LoginTypeEnum {
            return rawToEnum[raw] ?: NONE
        }
    }
}

class AccountManager private constructor() {
    // Public Inner Class, Struct, Enum, Interface

    // ----------------------------------------------------
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: AccountManager

        fun getInstance(): AccountManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = AccountManager()
                }
                return instance
            }
        }
    }

    // ----------------------------------------------------
    // Public Constant

    // ----------------------------------------------------
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private val compositeDisposable = CompositeDisposable()

    // Override Method or Basic Method
    // ----------------------------------------------------
    // Public Method
    fun getUserId(): Int {
        return PrefManager.getInstance().getIntValue(Pref.userId.name)
    }

    fun isLogin(): Boolean {
        if (PrefManager.getInstance().getStringValue(Pref.accessToken.name) == null) {
            return false
        }
        return true
    }

    fun processLogin(context: Context) {
        HHLog.d(TAG, "KEY_LOGIN_TYPE = ${PrefManager.getInstance().getStringValue(Pref.loginType.name)}")
        HHLog.d(TAG, "KEY_ACCESS_TOKEN_3RD = ${PrefManager.getInstance().getStringValue(Pref.accessToken3rd.name)}")

        BootingManager.getInstance().bootCompletedSubject
            .take(1)
            .flatMap {
                return@flatMap GlobalVariable.getInstance().commonRepository.login(
                    context,
                    PrefManager.getInstance().getStringValue(Pref.loginType.name)!!,
                    PrefManager.getInstance().getStringValue(Pref.accessToken3rd.name)!!,
                )
            }
            .subscribe({ loginModel ->
                HHLog.d(TAG, "loginModel = $loginModel")
                PrefManager.getInstance().setValue(
                    Pref.oldLoginType.name,
                    PrefManager.getInstance().getStringValue(Pref.loginType.name),
                )
                PrefManager.getInstance().setValue(Pref.userId.name, loginModel.id)
                PrefManager.getInstance().setValue(Pref.userName.name, loginModel.name)
                PrefManager.getInstance().setValue(Pref.userEmail.name, loginModel.email)
                PrefManager.getInstance().setValue(Pref.accessToken.name, loginModel.accessToken)
                PrefManager.getInstance().setValue(Pref.refreshToken.name, loginModel.refreshToken)
                processLoginWithScreen(context)
            }, { error ->
                HHLog.e(TAG, "error.message = ${error.message}")
                HHLog.e(TAG, "error = $error")
                if (error.message == HHError.MEMBER_NOT_FOUND.value) {
                    HHLog.d(TAG, "NO_MEMBER")
                    signup(context)
                    // gotoMapScene(context)
                }
            }).let { compositeDisposable.add(it) }
    }

    fun processLoginWithScreen(context: Context) {
        PushAlarmManager.getInstance().subscribe(PushAlarmManager.PushAlarmType.neccessary)

        val intent: Intent = Intent(context, MainActivity::class.java)
        (context as Activity).finish()
        context.startActivity(intent)
    }

    fun processLogout(context: Context) {
        PrefManager.getInstance().setValue(Pref.loginType.name, null)
        PrefManager.getInstance().setValue(Pref.accessToken3rd.name, null)
        PrefManager.getInstance().setValue(Pref.refreshToken3rd.name, null)
        PrefManager.getInstance().setValue(Pref.userEmail.name, null)
        PrefManager.getInstance().setValue(Pref.userName.name, null)
        PrefManager.getInstance().setValue(Pref.userId.name, 0)
        PrefManager.getInstance().setValue(Pref.accessToken.name, null)
        PrefManager.getInstance().setValue(Pref.refreshToken.name, null)

        PushAlarmManager.getInstance().unsubscribe(PushAlarmManager.PushAlarmType.neccessary)

    }

    fun signup(context: Context) {
        GlobalVariable.getInstance().commonRepository.signup(
            context,
            PrefManager.getInstance().getStringValue(Pref.loginType.name)!!,
            PrefManager.getInstance().getStringValue(Pref.accessToken3rd.name)!!,
        )
            .subscribe({ signupModel ->
                HHLog.d(TAG, "signupModel = $signupModel")
                PrefManager.getInstance().setValue(
                    Pref.oldLoginType.name,
                    PrefManager.getInstance().getStringValue(Pref.loginType.name),
                )
                PrefManager.getInstance().setValue(Pref.userId.name, signupModel.id)
                PrefManager.getInstance().setValue(Pref.userName.name, signupModel.name)
                PrefManager.getInstance().setValue(Pref.userEmail.name, signupModel.email)
                PrefManager.getInstance().setValue(Pref.accessToken.name, signupModel.accessToken)
                PrefManager.getInstance().setValue(Pref.refreshToken.name, signupModel.refreshToken)
                processLoginWithScreen(context)
            }, { error ->
                HHLog.e(TAG, "error = $error")
            }).let { compositeDisposable.add(it) }
    }
    // Private Method

    private fun gotoMapScene(context: Context) {

    }
}
