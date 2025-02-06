package com.constant.japaneseeveryday.extension

import android.widget.TextView

fun TextView.applyGUI(
    font: Int,
    textColor: Int,
) {
    setTextAppearance(font)
    setTextColor(context.getColor(textColor))
}
