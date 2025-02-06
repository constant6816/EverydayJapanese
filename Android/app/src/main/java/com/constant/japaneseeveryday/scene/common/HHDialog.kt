package com.constant.japaneseeveryday.scene.common

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.constant.japaneseeveryday.databinding.DialogHhdialogBinding
import com.constant.japaneseeveryday.extension.setTextOrHide

fun HHDialog(
    context: Context?,
    titleText: String? = null,
    contentsText: String? = null,
    okText: String? = null,
    okHandler: DialogInterface.OnClickListener? = null,
    cancelText: String? = null,
    cancelHandler: DialogInterface.OnClickListener? = null,
) {
    val binding =
        DialogHhdialogBinding.inflate(LayoutInflater.from(context)).apply {
            textviewTitle.setTextOrHide(titleText)
            textviewContent.setTextOrHide(contentsText)
            buttonPositive.setTextOrHide(okText)
            buttonNegative.setTextOrHide(cancelText)
            constraintlayout.clipToOutline = true
        }
    val builder = AlertDialog.Builder(context).setView(binding.root)
    val dialog: AlertDialog = builder.create()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCanceledOnTouchOutside(false)
    binding.buttonPositive.setOnClickListener {
        okHandler?.onClick(dialog, binding.buttonPositive.id)
        dialog.dismiss()
    }
    binding.buttonNegative.setOnClickListener {
        cancelHandler?.onClick(dialog, binding.buttonPositive.id)
        dialog.dismiss()
    }
    dialog.show()
}
