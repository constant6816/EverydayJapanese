package com.constant.japaneseeveryday.util

import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication

fun STR(resId: Int): String {
    return JapaneseEverydayApplication.context.getString(resId)
}

fun LATER(strText: String): String {
    return strText
}
