package com.constant.japaneseeveryday.singleton

import android.content.Context
import com.constant.japaneseeveryday.model.Configuration
import com.constant.japaneseeveryday.network.CommonRepository
import com.constant.japaneseeveryday.network.HHResponse
import com.constant.japaneseeveryday.network.entity.ReissueResponseEntity
import com.constant.japaneseeveryday.network.model.ReissueModel
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.HHStyle
import com.constant.japaneseeveryday.util.nonNull
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class ReissueManager {
    // Public Inner Class, Struct, Enum, Interface

    interface OneTimeAPI {
        // 로그인 할때
        @Headers("content-type:application/json")
        @POST("auth/reissue")
        fun reissue(
            @Body jsonObject: JsonObject,
        ): Call<ReissueResponseEntity>
    }

    // ----------------------------------------------------
    // companion object
    companion object {
        @Volatile
        private lateinit var instance: ReissueManager

        fun getInstance(): ReissueManager {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = ReissueManager()

                    val onetimeRetrofit =
                        Retrofit.Builder()
                            .baseUrl(Configuration.getInstance().getBaseUrl())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                    instance.onetimeAPI = onetimeRetrofit.create(OneTimeAPI::class.java)
                }
                return instance
            }
        }
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    private val EMPTY = true
    private val compositeDisposable = CompositeDisposable()

    // Public Variable
    // Private Variable
    private var onetimeAPI: OneTimeAPI? = null

    // reissue일 경우 skip을 위한 flag
    private var isReissueing = false

    // reissue가 끝나는 시점을 알기 위한 subject
    private var reissueSubject: PublishSubject<Boolean>? = null

    private lateinit var context: Context

    // Override Method or Basic Method
    // Public Method
    fun reissue(context: Context): Observable<Boolean> {
        this.context = context
        if (isReissueing) {
            HHLog.d(TAG, "skip reissue()")
            return Observable.create { emitor ->
                completeReissue(emitor)
            }
        }
        reissueSubject = PublishSubject.create()
        return Observable.create { emitor ->
            isReissueing = true
            val accessToken = nonNull(PrefManager.getInstance().getStringValue(Pref.accessToken.name))
            val refreshToken = nonNull(PrefManager.getInstance().getStringValue(Pref.refreshToken.name))
            reissue(accessToken, refreshToken)
                .subscribe({
                    PrefManager.getInstance().setValue(Pref.accessToken.name, it.accessToken)
                    PrefManager.getInstance().setValue(Pref.refreshToken.name, it.refreshToken)
                    emitor.onNext(EMPTY)
                    emitor.onComplete()
                    isReissueing = false
                    reissueSubject!!.onNext(EMPTY)
                }, { error ->
                    HHLog.e(TAG, "error = $error")
                    isReissueing = false
                    reissueSubject!!.onNext(EMPTY)
                }).let { compositeDisposable.add(it) }
        }
    }

    fun reissueBySync(
        context: Context,
        accessToken: String,
        refreshToken: String,
    ): Response<ReissueResponseEntity> {
        this.context = context

        val jsonObject = JsonObject()
        jsonObject.addProperty("accessToken", accessToken)
        jsonObject.addProperty("refreshToken", refreshToken)
        return onetimeAPI!!.reissue(jsonObject).execute()
    }

    // Private Method
    private fun reissue(
        accessToken: String,
        refreshToken: String,
    ): Observable<ReissueModel> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("accessToken", accessToken)
        jsonObject.addProperty("refreshToken", refreshToken)

        return reissue(jsonObject)
            .map { ReissueModel(it) }
    }

    private fun reissue(
        @Body jsonObject: JsonObject,
    ): Observable<ReissueResponseEntity> {
        return Observable.create { emitter ->
            onetimeAPI!!.reissue(
                jsonObject,
            ).enqueue(HHResponse<ReissueResponseEntity>(context, HHStyle(CommonRepository.Style.none), null, emitter))
        }
    }

    private fun completeReissue(emitor: ObservableEmitter<Boolean>) {
        reissueSubject!!
            .subscribe({
                HHLog.d(TAG, "completed reissue()")
                emitor.onNext(EMPTY)
                emitor.onComplete()
            }, { error ->
                HHLog.e(TAG, "error = $error")
            }).let { compositeDisposable.add(it) }
    }
}
