package com.constant.japaneseeveryday.extension

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.constant.japaneseeveryday.R

fun ImageView.setProfileImage(
    context: Context,
    profileImagePath: String?,
    isBig: Boolean,
    onClick: (() -> Unit)?,
) {
    scaleType = ImageView.ScaleType.CENTER_CROP

    if (profileImagePath == null) {
        if (isBig) {
            this.setImageResource(R.drawable.img_profile_default_big)
        } else {
            this.setImageResource(R.drawable.img_profile_default)
        }
    } else {
        Glide.with(context)
            .load(profileImagePath)
            .placeholder(R.drawable.img_placeholder)
            .into(this)
    }

    if (onClick != null) {
        isClickable = true
        setOnClickListener {
            onClick()
        }
    }
}
