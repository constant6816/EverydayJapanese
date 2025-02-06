package com.constant.japaneseeveryday.network

import android.content.Context
import com.constant.japaneseeveryday.singleton.Pref
import com.constant.japaneseeveryday.singleton.PrefManager
import com.constant.japaneseeveryday.singleton.ReissueManager
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.HHResponseCode
import com.constant.japaneseeveryday.util.nonNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class ErrorHandleInterceptor(private val context: Context) : Interceptor {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    companion object {
        private const val EMPTY_JSON = "{}"

        private const val KEY_MESSAGE = "responseMessage"
        private const val KEY_DATA = "responseData"

        private const val AUTHORIZATION = "authorization"
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    // Public Variable
    // Private Variable
    // Override Method or Basic Method

    override fun intercept(chain: Interceptor.Chain): Response =
        with(chain) {
            val originalRequest = request()
            val response = chain.proceed(originalRequest)

            return try {
                when (response.code) {
                    HHResponseCode.UNAUTHORIZED.value -> {
                        HHLog.d(TAG, "ACCESS TOKEN ERROR")
                        if (reissue()) {
                            HHLog.d(TAG, "reissue OK")
                            response.close()
                            val accessToken = nonNull(PrefManager.getInstance().getStringValue(Pref.accessToken.name))
                            HHLog.d(TAG, "after reissue, accessToken = $accessToken")
                            val request2 =
                                originalRequest.recomposeHeader(
                                    nonNull(PrefManager.getInstance().getStringValue(Pref.accessToken.name)),
                                )
                            val response2 = chain.proceed(request2)
                            if (response2.isSuccessful) {
                                HHLog.d(TAG, "request, response is OK")
                                return response2
                            } else {
                                return makeResponse(response2)
                            }
                        } else {
                            return response
                        }
                    }
                    in 200..299 -> response
                    else -> response
                }
            } catch (t: Throwable) {
                response
            }
        }

    // Public Method
    // Private Method
    private fun reissue(): Boolean {
        val accessToken = nonNull(PrefManager.getInstance().getStringValue(Pref.accessToken.name))
        val refreshToken = nonNull(PrefManager.getInstance().getStringValue(Pref.refreshToken.name))

        val response = ReissueManager.getInstance().reissueBySync(context, accessToken, refreshToken)
        HHLog.d(TAG, "reissue")
        if (response.code() == HHResponseCode.OK.value) {
            HHLog.d(TAG, "reissue is OK")
            val reissueEntity = response.body()
            HHLog.d(TAG, "accessToken = ${reissueEntity!!.responseData!!.accessToken}")
            HHLog.d(TAG, "refreshToken = ${reissueEntity!!.responseData!!.refreshToken}")
            PrefManager.getInstance().setValue(Pref.accessToken.name, reissueEntity!!.responseData!!.accessToken)
            PrefManager.getInstance().setValue(Pref.refreshToken.name, reissueEntity!!.responseData!!.refreshToken)
            return true
        }
        HHLog.d(TAG, "reissue is not ok")
        return false
    }

    private fun Request.recomposeHeader(accessToken: String): Request {
        return this.newBuilder()
            .removeHeader(AUTHORIZATION)
            .addHeader(AUTHORIZATION, "Bearer $accessToken")
            .build()
    }

    private fun makeResponse(response: Response): Response {
        HHLog.d(TAG, "makeResponse 1")
        val responseJson = response.extractResponseJson()
        val dataPayload =
            when (responseJson.has(KEY_DATA)) {
                true -> responseJson[KEY_DATA]
                false -> EMPTY_JSON
            }
        HHLog.d(TAG, "makeResponse 2")
        return when {
            (responseJson.has(KEY_DATA) && responseJson[KEY_DATA].toString() == "null") -> {
                response
            }
            else ->
                response.newBuilder()
                    .message(responseJson[KEY_MESSAGE].toString())
                    .body(dataPayload.toString().toResponseBody())
                    .build()
        }
    }

    private fun Response.extractResponseJson(): JSONObject {
        val jsonString = this.peekBody(Long.MAX_VALUE)?.string()
        return try {
            JSONObject(jsonString)
        } catch (exception: Exception) {
            HHLog.e(TAG, "exception = $exception")
            throw Exception()
        }
    }
}
