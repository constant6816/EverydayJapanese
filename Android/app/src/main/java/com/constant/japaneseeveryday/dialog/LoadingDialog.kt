package com.constant.japaneseeveryday.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.databinding.DialogLoadingBinding
import com.constant.japaneseeveryday.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class LoadingDialog(context: Context) : Dialog(context) {
    // Public Inner Class, Struct, Enum, Interface
    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: DialogLoadingBinding
    private var description: String? = null

    // Override Method or Basic Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeVariables()
        initializeViews()
    }

    // Public Method
    fun update(
        description: String?,
        isShow: Boolean,
    ) {
        this.description = description
        if (isShow) {
            showDescription()
        }
    }

    // Private Method
    private fun initializeVariables() {
    }

    private fun initializeViews() {
        binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        binding.apply {
            lottieanimationviewIcon.clipToOutline = true
        }
        setCanceledOnTouchOutside(false)
        showDescription()
    }

    private fun showDescription() {
        if (description == null) {
            setBackground(R.color.transparent)
            binding.textviewContent.visibility = View.GONE
        } else {
            setBackground(R.color.fg_black_0_middle_alpha)
            binding.linearlayout.clipToOutline = true
            binding.textviewContent.visibility = View.VISIBLE
            binding.textviewContent.setText(description)
        }
    }

    private fun setBackground(color: Int) {
        val background = binding.linearlayout.background
        if (background != null) {
            val wrappedDrawable = DrawableCompat.wrap(background)
            DrawableCompat.setTint(
                wrappedDrawable,
                ContextCompat.getColor(context, color),
            )
        }
    }
}
