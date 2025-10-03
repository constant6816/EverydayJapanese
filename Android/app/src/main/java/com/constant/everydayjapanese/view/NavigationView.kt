package com.constant.everydayjapanese.view

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginRight
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.util.HHLog
import com.constant.everydayjapanese.util.HHStyle
import com.constant.everydayjapanese.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class NavigationView : FrameLayout {
    // Public Inner Class, Struct, Enum, Interface
    interface OnButtonClickListener {
        fun onClick(id: Int)
    }

    enum class ButtonType(val id: Int) {
        back(0),
        close(1),
        menu(2),
        custom1(3),
        custom2(4),
        custom3(5),
        custom4(6),
    }

    enum class Background {
        transparent,
        opacity,
        opacityLight,
    }

    object ButtonId {
        val none = 0
        val leftBack = 1 shl 0
        val leftClose = 1 shl 1
    }

    object Style {
        val none = 0
        val progress = 1 shl 0
        val onlyTitle = 1 shl 1
    }

    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    lateinit var buttonLeftBack: ImageButton
    lateinit var buttonLeftClose: ImageButton
    lateinit var textviewTitle: TextView
    lateinit var textviewDescription: TextView
    lateinit var imageviewIcon: ImageView

    // ----------------------------------------------------
    // Private Variable
    private var onButtonClickListener: OnButtonClickListener? = null
    private var buttonStyle: HHStyle
    private var style: HHStyle
    private var backgroundType: Background

    private lateinit var viewUnderline: View

    // Override Method or Basic Method
    constructor(context: Context) : super(context) {
        buttonStyle = HHStyle(ButtonId.leftBack)
        style = HHStyle(Style.none)
        backgroundType = Background.opacity

        initializeViews()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        buttonStyle = HHStyle(ButtonId.leftBack)
        style = HHStyle(Style.none)
        backgroundType = Background.opacity

        initializeViews()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        buttonStyle = HHStyle(ButtonId.leftBack)
        style = HHStyle(Style.none)
        backgroundType = Background.opacity

        initializeViews()
    }

    // Public Method
    fun setButtonStyle(buttonStyle: HHStyle) {
        this.buttonStyle = buttonStyle
        reloadButtonStyle()
    }

    fun setStyle(style: HHStyle) {
        this.style = style
        reloadStyle()
    }

    fun setBackgroundType(backgroundType: Background) {
        this.backgroundType = backgroundType
        reloadBackgroundType()
    }

    fun set(
        title: String,
        description: String,
        resourceId: Int,
    ) {
        textviewTitle.text = title
        textviewDescription.text = description
        imageviewIcon.setImageResource(resourceId)
    }

    fun setOnButtonClickListener(onButtonClickListener: OnButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener
    }

    // Private Method
    private fun initializeViews() {
        HHLog.d(TAG, "initializeViews")
        if (context == null) {
            return
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        inflater?.inflate(R.layout.view_navigation, this, true)

        buttonLeftBack = findViewById(R.id.button_left_back)
        buttonLeftClose = findViewById(R.id.button_left_close)
        textviewTitle = findViewById(R.id.textview_title)
        textviewDescription = findViewById(R.id.textview_description)
        imageviewIcon = findViewById(R.id.imageview_icon)

        buttonLeftBack.setOnClickListener {
            onButtonClickListener?.onClick(ButtonType.back.id)
        }

        buttonLeftClose.setOnClickListener {
            onButtonClickListener?.onClick(ButtonType.close.id)
        }

        expandTouchArea(buttonLeftBack)
        expandTouchArea(buttonLeftClose)

        // buttonRightCustom.setTextAppearance(R.style.font_h1)

        reloadButtonStyle()
        reloadStyle()
        reloadBackgroundType()
    }

    private fun reloadButtonStyle() {
        buttonLeftBack.visibility = if (buttonStyle.isInclude(ButtonId.leftBack)) View.VISIBLE else View.GONE
        buttonLeftClose.visibility = if (buttonStyle.isInclude(ButtonId.leftClose)) View.VISIBLE else View.GONE
    }

    private fun reloadStyle() {
//        viewUnderline.visibility = if (style.isInclude(Style.underline)) View.VISIBLE else View.GONE
//        if style.isInclude(NavigationView.Style.collapsingToolbarLayout) {
//            if scrollViewContentOffsetY <= titleHiddenHeight {
//                lbTitle.alpha = 0
//            } else {
//                lbTitle.alpha = 1
//            }
//        }
        if (style.isInclude(NavigationView.Style.onlyTitle)) {
            val marginInDp = 10
            val scale = textviewTitle.context.resources.displayMetrics.density
            val marginInPx = (marginInDp * scale).toInt()
            val params = textviewTitle.layoutParams as ViewGroup.MarginLayoutParams
            params.rightMargin = marginInPx
            textviewTitle.layoutParams = params

            textviewDescription.visibility = View.GONE
            imageviewIcon.visibility = View.GONE
        }
    }

    private fun reloadBackgroundType() {
        when (backgroundType) {
            Background.transparent -> {
                setBackgroundColor(Color.TRANSPARENT)
                textviewTitle.setTextColor(context.getColor(R.color.fg_white1))
                buttonLeftBack.setImageResource(R.drawable.btn_back_dark)
            }
            Background.opacity -> {
                setBackgroundColor(context.getColor(R.color.bg3))
                textviewTitle.setTextColor(context.getColor(R.color.fg1))
                buttonLeftBack.setImageResource(R.drawable.btn_back_dark)
            }
            Background.opacityLight -> {
                setBackgroundColor(context.getColor(R.color.bg3))
                textviewTitle.setTextColor(context.getColor(R.color.fg_black1))
                buttonLeftBack.setImageResource(R.drawable.btn_back_dark)
            }
            else -> {
            }
        }
    }

    private fun expandTouchArea(button: ImageButton) {
        button.post {
            // 부모 뷰를 가져옵니다
            val parent = button.parent as View

            // 터치 영역을 확장합니다
            parent.post {
                val rect = Rect()
                button.getHitRect(rect)
                val padding = 50
                rect.top -= padding
                rect.bottom += padding
                rect.left -= padding
                rect.right += padding
                parent.touchDelegate = TouchDelegate(rect, button)
            }
        }
    }
}
