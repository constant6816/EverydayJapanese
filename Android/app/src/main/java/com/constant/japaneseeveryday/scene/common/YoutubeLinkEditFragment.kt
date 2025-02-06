package com.constant.japaneseeveryday.scene.common

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.databinding.BottomsheetYoutubeLinkEditBinding
import com.constant.japaneseeveryday.extension.getYoutubeId
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.nonNull
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class YoutubeLinkEditFragment : BottomSheetDialogFragment() {
    // Public Inner Class, Struct, Enum, Interface
    interface OnOkHandler {
        fun onOkHandler(
            fullUrl: String,
            youtubeId: String,
        )
    }

    // companion object
    companion object {
        public val EXTRA_YOUTUBE_ID_ARRAY = "EXTRA_YOUTUBE_ID_ARRAY"
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: BottomsheetYoutubeLinkEditBinding
    private var onOkHandler: OnOkHandler? = null
    private var youtubeIds: ArrayList<String> = ArrayList<String>()

    // Override Method or Basic Method
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initializeVariables()
        initializeViews(inflater, container)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            HHLog.d(TAG, "onViewCreated()")
            edittextYoutubeLink.requestFocus()
        }
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    // Public Method
    fun setOnOkHandler(onOkHandler: OnOkHandler) {
        this.onOkHandler = onOkHandler
    }

    // Private Method
    private fun initializeVariables() {
    }

    private fun initializeViews(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        arguments?.getStringArrayList(EXTRA_YOUTUBE_ID_ARRAY)?.let {
            youtubeIds = it
        }

        binding =
            DataBindingUtil.inflate<BottomsheetYoutubeLinkEditBinding?>(inflater, R.layout.bottomsheet_youtube_link_edit, container, false).apply {
                edittextYoutubeLink.addTextChangedListener(
                    object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int,
                        ) {
                        }

                        override fun onTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int,
                        ) {
                            textviewError.visibility = View.INVISIBLE
                        }

                        override fun afterTextChanged(p0: Editable?) {
                        }
                    },
                )
                textviewError.visibility = View.INVISIBLE
                buttonInsert.setOnClickListener {
                    val fullurl = edittextYoutubeLink.text.toString()
                    val youtubeId = fullurl.getYoutubeId()
                    if (youtubeId == null) {
                        textviewError.text = getText(R.string.board_youtube_link_error_message)
                        textviewError.visibility = View.VISIBLE
                    } else {
                        if (youtubeIds.contains(youtubeId)) {
                            textviewError.text = getText(R.string.board_youtube_already_insert)
                            textviewError.visibility = View.VISIBLE
                        } else {
                            dismiss()
                            onOkHandler?.onOkHandler(fullurl, youtubeId)
                        }
                    }
                }
            }
    }
}
