package com.constant.japaneseeveryday.network

import android.content.Context
import com.constant.japaneseeveryday.dialog.LoadingDialog
import com.constant.japaneseeveryday.network.entity.AddressResponseEntity
import com.constant.japaneseeveryday.util.HHStyle
import com.constant.japaneseeveryday.util.nonNull
import io.reactivex.rxjava3.core.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class KakaoAPI(private val context: Context, private val style: HHStyle) {
    // ----------------------------------------------------
    // Public Inner Class, Struct, Enum, Interface
    interface RestAPI {
        // ----- 기본 -------
        // Configuration 최초 사용 함수
        @GET("v2/local/geo/coord2regioncode.json")
        fun getReverseGeoCoding(
            @Query("y")latitude: Double,
            @Query("x")longitude: Double,
        ): Call<AddressResponseEntity>
    }

    // companion object
    // Public Constant

    // ----------------------------------------------------
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    private val CONNECT_TIMEOUT = 10L
    private val WRITE_TIMEOUT = 30L
    private val READ_TIMEOUT = 30L

    // Public Variable

    // ----------------------------------------------------
    // Private Variable

    // ----------------------------------------------------
    // Override Method or Basic Method

    // ----------------------------------------------------
    // Public Method
    fun getReverseGeoCoding(
        @Query("y")latitude: Double,
        @Query("x")longitude: Double,
    ): Observable<AddressResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }

        return Observable.create { emitter ->
            createRetrofit().getReverseGeoCoding(
                latitude,
                longitude,
            ).enqueue(HHResponse<AddressResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    // Private Method
    private fun createRetrofit(): RestAPI {
        val client =
            OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(createHeader())
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                )
                .build()

        val retrofit =
            Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(RestAPI::class.java)
    }

    private fun createHeader(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            requestBuilder
                .addHeader("Authorization", "KakaoAK 3498da7643e3715ed2116bf17315a04c")
            return@Interceptor it.proceed(requestBuilder.build())
        }
    }
}
