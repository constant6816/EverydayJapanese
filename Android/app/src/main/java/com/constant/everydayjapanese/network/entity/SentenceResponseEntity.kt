package com.constant.everydayjapanese.network.entity

import com.google.gson.annotations.SerializedName

class SentenceResponseEntity {
    inner class ResponseDataEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("text")
        val text: String? = null

        @SerializedName("transcriptions")
        val transcriptions: Array<TranscriptionEntity>? = null

        @SerializedName("translations")
        val translations: Array<TranslationEntity>? = null
    }

    inner class TranscriptionEntity {
        @SerializedName("text")
        val text: String? = null

        @SerializedName("html")
        val html: String? = null
    }

    inner class TranslationEntity {
        @SerializedName("id")
        val id: Int? = null

        @SerializedName("text")
        val text: String? = null
    }

    @SerializedName("data")
    val data: Array<ResponseDataEntity>? = null
}
