package com.constant.everydayjapanese.scene.main

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.databinding.ActivityHiraganaKatakanaPracticeBinding
import com.constant.everydayjapanese.util.Coordinate
import com.constant.everydayjapanese.util.IndexEnum
import com.constant.everydayjapanese.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class HiraganaKatakanaPracticeActivity : AppCompatActivity() {
    // Public Inner Class, Struct, Enum, Interface
    data class Param(
        var indexEnum: IndexEnum,
    )

    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: ActivityHiraganaKatakanaPracticeBinding
    private lateinit var param: Param

    // Override Method or Basic Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeVariables()
        initializeViews()
    }

    // Public Method
    // Private Method
    private fun initializeVariables() {
    }

    private fun initializeViews() {
        binding = ActivityHiraganaKatakanaPracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            textviewMain.text = "가"
            textviewSub.text = "나다"

            val layoutParams = framelayoutCharacter.layoutParams ?: ViewGroup.LayoutParams(0, 0)
            layoutParams.height = Coordinate.getWidth() - 2 * resources.getDimensionPixelSize(R.dimen.space_m)
            framelayoutCharacter.layoutParams = layoutParams
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }
    }
}
