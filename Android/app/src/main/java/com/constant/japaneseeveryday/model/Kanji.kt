package com.constant.japaneseeveryday.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Kanji(
    val kanji: String,
    val hanja: String,
    val eumhun: String,
    val jpSound: String,
    val jpMeaning: String,
    val examples: List<Vocabulary>
) : Parcelable {

}