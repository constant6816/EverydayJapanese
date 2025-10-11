package com.constant.everydayjapanese.scene.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.databinding.ActivityStudyBinding
import com.constant.everydayjapanese.model.Kanji
import com.constant.everydayjapanese.model.Vocabulary
import com.constant.everydayjapanese.singleton.JSONManager
import com.constant.everydayjapanese.singleton.Pref
import com.constant.everydayjapanese.singleton.PrefManager
import com.constant.everydayjapanese.singleton.TTSManager
import com.constant.everydayjapanese.util.HHLog
import com.constant.everydayjapanese.util.HHStyle
import com.constant.everydayjapanese.util.IndexEnum
import com.constant.everydayjapanese.util.SectionEnum
import com.constant.everydayjapanese.util.nonNull
import com.constant.everydayjapanese.view.ColorDivider
import com.constant.everydayjapanese.view.NavigationView

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class StudyActivity : AppCompatActivity() {
    // Public Inner Class, Struct, Enum, Interface
    interface OnSelectItemListener {
        fun onSelectItem(position: Int)
    }

    data class Param(
        var indexEnum: IndexEnum,
        var dayTitle: String,
        var dayKey: String,
        var kanjis: List<Kanji>?,
        var vocabularies: List<Vocabulary>?,
    )

    data class KanjiForCell(
        var kanji: Kanji,
        var isVisible: Boolean = false,
        var isVisibleHanja: Boolean = false,
        var isBookmark: Boolean = false,
        var isExpanded: Boolean = false,
    )

    data class VocabularyForCell(
        var vocabulary: Vocabulary,
        var isVisible: Boolean = false,
        var isBookmark: Boolean = false,
        var isExpanded: Boolean = false,
        var exampleText: String? = null,
        var transText: String? = null,
    )

    // companion object
    companion object {
        public val EXTRA_INDEX_ENUM = "EXTRA_INDEX_ENUM"
        public val EXTRA_DAY_TITLE = "EXTRA_DAY_TITLE"
        public val EXTRA_DAY_KEY = "EXTRA_DAY_KEY"
        public val EXTRA_KANJIS_DAY_DISTRIBUTED = "EXTRA_KANJIS_DAY_DISTRIBUTED"
        public val EXTRA_VOCABULARIES_DAY_DISTRIBUTED = "EXTRA_VOCABULARIES_DAY_DISTRIBUTED"
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: ActivityStudyBinding
    private var kanjiAdapter: KanjiAdapter? = null
    private var vocabularyAdapter: VocabularyAdapter? = null

    // Param
    private lateinit var param: Param
    private var kanjisForCell: List<KanjiForCell>? = null
    private var vocabulariesForCell: List<VocabularyForCell>? = null
    private var kanjiBookmarks: HashSet<Kanji> = HashSet<Kanji>()
    private var vocabularyBookmarks: HashSet<Vocabulary> = HashSet<Vocabulary>()

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
                getIntent().getParcelableArrayListExtra<Kanji>(EXTRA_KANJIS_DAY_DISTRIBUTED),
                getIntent().getParcelableArrayListExtra<Vocabulary>(EXTRA_VOCABULARIES_DAY_DISTRIBUTED),
            )
        kanjisForCell =
            param.kanjis?.map { kanjis ->
                return@map KanjiForCell(kanjis)
            }

        PrefManager.getInstance().getStringValue(
            Pref.kanjiBookmark.name,
        )?.let {
            kanjiBookmarks = JSONManager.getInstance().decodeJSONtoKanjiSet(it)
        }

        kanjisForCell?.forEach { kanjiForCell ->
            kanjiForCell.isBookmark = kanjiBookmarks.contains(kanjiForCell.kanji)
        }

        vocabulariesForCell =
            param.vocabularies?.map { vocabularies ->
                return@map VocabularyForCell(vocabularies)
            }

        PrefManager.getInstance().getStringValue(
            Pref.vocabularyBookmark.name,
        )?.let {
            vocabularyBookmarks = JSONManager.getInstance().decodeJSONtoVocabularySet(it)
        }

        vocabulariesForCell?.forEach { vocabularyForCell ->
            vocabularyForCell.isBookmark = vocabularyBookmarks.contains(vocabularyForCell.vocabulary)
        }

        // 초기화 때문에 필요
        TTSManager.getInstance()
    }

    private fun initializeViews() {
        binding = ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            navigationview.set(
                nonNull(param.indexEnum.getSection()?.title),
                nonNull(param.indexEnum.title) + " " + param.dayTitle,
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

            buttonShuffle.setOnClickListener {
                when (param.indexEnum.getSection()) {
                    SectionEnum.kanji -> {
                        kanjiAdapter?.shuffle()
                    }
                    SectionEnum.vocabulary -> {
                        vocabularyAdapter?.shuffle()
                    }
                    else -> {
                        HHLog.d(TAG, "do nothing!")
                    }
                }
            }

            buttonAllVisible.setOnClickListener {
                when (param.indexEnum.getSection()) {
                    SectionEnum.kanji -> {
                        kanjiAdapter?.toggleAllVisible()
                    }
                    SectionEnum.vocabulary -> {
                        vocabularyAdapter?.toggleAllVisible()
                    }
                    else -> {
                        HHLog.d(TAG, "do nothing!")
                    }
                }
            }

            buttonTest.setOnClickListener {
                val intent = Intent(this@StudyActivity, TestActivity::class.java)
                intent.putExtra(TestActivity.EXTRA_INDEX_ENUM, param.indexEnum.id)
                intent.putExtra(TestActivity.EXTRA_DAY_TITLE, param.dayTitle)
                intent.putExtra(TestActivity.EXTRA_DAY_KEY, param.dayKey)
                when (param.indexEnum.getSection()) {
                    SectionEnum.kanji -> {
                        param.kanjis?.let { kanjis ->
                            intent.putExtra(TestActivity.EXTRA_KANJIS, ArrayList(kanjis))
                            startActivity(intent)
                        }
                    }
                    SectionEnum.vocabulary -> {
                        param.vocabularies?.let { vocabularies ->
                            intent.putExtra(TestActivity.EXTRA_VOCABULARIES, ArrayList(vocabularies))
                            startActivity(intent)
                        }
                    }
                    else -> {
                        HHLog.d(TAG, "do nothing!")
                    }
                }
            }

            if (param.indexEnum.getSection() == SectionEnum.kanji || 0 < nonNull(kanjisForCell?.size)) {
                kanjisForCell?.let { kanjisForCell ->
                    kanjiAdapter = KanjiAdapter(this@StudyActivity, kanjisForCell, kanjiBookmarks)
                    kanjiAdapter?.setOnSelectItemListener(
                        object : OnSelectItemListener {
                            override fun onSelectItem(position: Int) {
                                kanjisForCell.get(position).isVisible = !kanjisForCell.get(position).isVisible
                                kanjiAdapter?.notifyItemChanged(position)
                            }
                        },
                    )
                    recyclerview.adapter = kanjiAdapter
                }
            } else if (param.indexEnum.getSection() == SectionEnum.vocabulary || 0 < nonNull(vocabulariesForCell?.size))
                { // vocabulary
                    vocabulariesForCell?.let { vocabulariesForCell ->
                        vocabularyAdapter = VocabularyAdapter(this@StudyActivity, vocabulariesForCell, vocabularyBookmarks)
                        vocabularyAdapter?.setOnSelectItemListener(
                            object : OnSelectItemListener {
                                override fun onSelectItem(position: Int) {
                                    vocabulariesForCell.get(position)?.isVisible = !vocabulariesForCell.get(position).isVisible
                                    vocabularyAdapter?.notifyItemChanged(position)
                                }
                            },
                        )
                        recyclerview.adapter = vocabularyAdapter
                    }
                }
            recyclerview.addItemDecoration(
                ColorDivider(1f, 10f, this@StudyActivity.getColor(R.color.bg4)) {
                    true
                },
            )
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }
    }
}
