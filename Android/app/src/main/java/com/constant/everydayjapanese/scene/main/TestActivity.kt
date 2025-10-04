package com.constant.everydayjapanese.scene.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.databinding.ActivityTestBinding
import com.constant.everydayjapanese.model.Kanji
import com.constant.everydayjapanese.model.Vocabulary
import com.constant.everydayjapanese.util.HHLog
import com.constant.everydayjapanese.util.HHStyle
import com.constant.everydayjapanese.util.IndexEnum
import com.constant.everydayjapanese.util.SectionEnum
import com.constant.everydayjapanese.util.nonNull
import com.constant.everydayjapanese.view.NavigationView

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class TestActivity : AppCompatActivity() {
    // Public Inner Class, Struct, Enum, Interface
    data class Param(
        var indexEnum: IndexEnum,
        var dayTitle: String,
        var dayKey: String,
        var kanjis: ArrayList<Kanji>?,
        var vocabularies: ArrayList<Vocabulary>?,
    )

    // companion object
    companion object {
        public val EXTRA_INDEX_ENUM = "EXTRA_INDEX_ENUM"
        public val EXTRA_DAY_TITLE = "EXTRA_DAY_TITLE"
        public val EXTRA_DAY_KEY = "EXTRA_DAY_KEY"
        public val EXTRA_KANJIS = "EXTRA_KANJIS"
        public val EXTRA_VOCABULARIES = "EXTRA_VOCABULARIES"
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: ActivityTestBinding
    private lateinit var param: Param
    private var index: Int = 0
    private var isVisible = false
    private var testResults = ArrayList<Boolean>()
    private var kanjis: ArrayList<Kanji>? = null
    private var vocabularies: ArrayList<Vocabulary>? = null

    // Override Method or Basic Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeVariables()
        initializeViews()
    }

    // Public Method
    // Private Method

    private fun initializeVariables() {
        param =
            Param(
                IndexEnum.ofRaw(getIntent().getIntExtra(EXTRA_INDEX_ENUM, 0)),
                nonNull(getIntent().getStringExtra(EXTRA_DAY_TITLE)),
                nonNull(getIntent().getStringExtra(EXTRA_DAY_KEY)),
                getIntent().getParcelableArrayListExtra<Kanji>(EXTRA_KANJIS),
                getIntent().getParcelableArrayListExtra<Vocabulary>(EXTRA_VOCABULARIES),
            )

        kanjis = param.kanjis
        vocabularies = param.vocabularies
        kanjis?.shuffle()
        vocabularies?.shuffle()
        testResults.clear()
    }

    private fun initializeViews() {
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            navigationview.set(
                nonNull(param.indexEnum.getSection()?.title),
                String.format("%s %s %s", nonNull(param.indexEnum.title), param.dayTitle, getString(R.string.test)),
                param.indexEnum.getResourceId(),
            )
            navigationview.setButtonStyle(HHStyle(NavigationView.ButtonId.leftBack))
            navigationview.setOnButtonClickListener(
                object : NavigationView.OnButtonClickListener {
                    override fun onClick(id: Int) {
                        when (id) {
                            NavigationView.ButtonType.back.id -> {
                                finish()
                            }
                            else -> {
                            }
                        }
                    }
                },
            )
            buttonCorrect.setOnClickListener {
                testResults.add(true)
                moveOnToNext()
            }
            buttonWrong.setOnClickListener {
                testResults.add(false)
                moveOnToNext()
            }
            buttonPrevious.setOnClickListener {
                moveOnToPrevious()
            }
            relativelayoutCard.setOnClickListener {
                HHLog.d(TAG, "onClick")
                if (textviewUpperSub1.visibility == View.VISIBLE) {
                    showText(false)
                } else {
                    showText(true)
                }
            }
            showText(false)
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }
        updateTestField()
    }

    private fun updateTestField() {
        if (index == 0) {
            binding.buttonPrevious.visibility = View.INVISIBLE
        } else {
            binding.buttonPrevious.visibility = View.VISIBLE
        }

        when (param.indexEnum.getSection()) {
            SectionEnum.kanji -> {
                param.kanjis?.let { kanjis ->
                    binding.textviewIndex.text = "${index + 1}/${kanjis.size}"
                    val kanji = kanjis.get(index)
                    binding.textviewUpperSub1.text = kanji.jpSound
                    binding.textviewUpperSub2.text = kanji.jpMeaning
                    binding.textviewMain.text = kanji.kanji
                    binding.textviewLowerSub.text = kanji.eumhun
                }
            }
            SectionEnum.vocabulary -> {
                param.vocabularies?.let { vocabularies ->
                    binding.textviewIndex.text = "${index + 1}/${vocabularies.size}"
                    val vocabulary = vocabularies.get(index)
                    binding.textviewUpperSub1.text = ""
                    binding.textviewUpperSub2.text = vocabulary.sound
                    binding.textviewMain.text = vocabulary.word
                    binding.textviewLowerSub.text = vocabulary.meaning
                }
            }
            else -> {
            }
        }
        showText(false)
    }

    private fun moveOnToNext() {
        index += 1
        if ((param.indexEnum.getSection() == SectionEnum.kanji && index == kanjis?.size) ||
            (param.indexEnum.getSection() == SectionEnum.vocabulary && index == vocabularies?.size)
        ) {
            val intent = Intent(this@TestActivity, TestResultActivity::class.java)
            intent.putExtra(TestResultActivity.EXTRA_INDEX_ENUM, param.indexEnum.id)
            intent.putExtra(TestResultActivity.EXTRA_DAY_TITLE, param.dayTitle)
            intent.putExtra(TestResultActivity.EXTRA_DAY_KEY, param.dayKey)
            when (param.indexEnum.getSection()) {
                SectionEnum.kanji -> {
                    intent.putExtra(TestResultActivity.EXTRA_ALL_COUNT, kanjis!!.size)
                    var wrongKanjis = ArrayList<Kanji>()
                    testResults.forEachIndexed { index, testResult ->
                        if (testResult == false) {
                            wrongKanjis.add(kanjis!!.get(index))
                        }
                    }
                    HHLog.d(TAG, "wrongKanjis = $wrongKanjis")
                    intent.putExtra(TestResultActivity.EXTRA_KANJIS, wrongKanjis)
                }
                SectionEnum.vocabulary -> {
                    intent.putExtra(TestResultActivity.EXTRA_ALL_COUNT, vocabularies?.size)
                    var wrongVocabularies = ArrayList<Vocabulary>()
                    testResults.forEachIndexed { index, testResult ->
                        if (testResult == false) {
                            wrongVocabularies.add(vocabularies!!.get(index))
                        }
                    }
                    intent.putExtra(TestResultActivity.EXTRA_VOCABULARIES, wrongVocabularies)
                }
                else -> {
                }
            }
            startActivity(intent)
            finish()
            return
        }
        isVisible = false
        updateTestField()
    }

    private fun moveOnToPrevious() {
        if (index == 0) {
            return
        }
        index -= 1
        testResults.removeLast()
        isVisible = false
        updateTestField()
    }

    private fun showText(isShow: Boolean) {
        if (isShow) {
            binding.textviewUpperSub1.visibility = View.VISIBLE
            binding.textviewUpperSub2.visibility = View.VISIBLE
            binding.textviewLowerSub.visibility = View.VISIBLE
        } else {
            binding.textviewUpperSub1.visibility = View.INVISIBLE
            binding.textviewUpperSub2.visibility = View.INVISIBLE
            binding.textviewLowerSub.visibility = View.INVISIBLE
        }
    }
}
