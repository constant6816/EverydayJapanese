package com.constant.japaneseeveryday.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Vocabulary(
    val word: String,
    val sound: String,
    val meaning: String
) : Parcelable {

}