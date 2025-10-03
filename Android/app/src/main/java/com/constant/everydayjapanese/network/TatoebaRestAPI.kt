package com.constant.everydayjapanese.network

import android.content.Context
import com.constant.everydayjapanese.dialog.LoadingDialog
import com.constant.everydayjapanese.network.entity.*
import com.constant.everydayjapanese.util.*
import io.reactivex.rxjava3.core.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class TatoebaRestAPI(private val context: Context, private val style: HHStyle) {
    // ----------------------------------------------------
    // Public Inner Class, Struct, Enum, Interface
    interface RestAPI {
        @GET("/unstable/sentences")
        fun getSentence(
            @Query("lang")lang: String,
            @Query("q")q: String,
            @Query("limit")limit: Int,
            @Query("trans:lang")trans: String,
            @Query("sort")sort: String,
            @Query("showtrans:lang")showtrans: String,
        ): Call<SentenceResponseEntity>
    }

    // companion object
    // Public Constant

    // ----------------------------------------------------
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    private val CONNECT_TIMEOUT = 10L
    private val WRITE_TIMEOUT = 60L
    private val READ_TIMEOUT = 60L

    // Public Variable

    // ----------------------------------------------------
    // Private Variable

    // ----------------------------------------------------
    // Override Method or Basic Method

    // ----------------------------------------------------
    // Public Method
    fun getSentence(
        @Query("lang")lang: String,
        @Query("q")q: String,
        @Query("limit")limit: Int,
        @Query("trans:lang")trans: String,
        @Query("sort")sort: String,
        @Query("showtrans:lang")showtrans: String,
    ): Observable<SentenceResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(TatoebaRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }

        return Observable.create { emitter ->
            createRetrofit(
                false,
            ).getSentence(
                lang,
                q,
                limit,
                trans,
                sort,
                showtrans,
            ).enqueue(HHResponse<SentenceResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    // Private Method
    private fun createRetrofit(isSecured: Boolean): RestAPI {
        val client =
            OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(createHeader(isSecured))
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                )
                .build()

        val retrofit =
            Retrofit.Builder() // 레트로핏 빌더 생성(생성자 호출)
                .baseUrl("https://api.tatoeba.org") // 빌더 객체의 baseURL호출=서버의 메인 URL정달
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()) // gson컨버터 연동
                .build() // 객체반환

        return retrofit.create(RestAPI::class.java) // SampleService 인터페이스를 구현한 구현체
    }

    private fun createHeader(isSecured: Boolean): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()

            requestBuilder
                .addHeader("content-type", "application/json;charset=utf-8")

            return@Interceptor it.proceed(requestBuilder.build())
        }
    }
}
