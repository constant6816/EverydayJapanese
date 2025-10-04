package com.constant.everydayjapanese.scene.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.databinding.ActivityTestResultBinding
import com.constant.everydayjapanese.model.Kanji
import com.constant.everydayjapanese.model.Vocabulary
import com.constant.everydayjapanese.scene.main.StudyActivity.KanjiForCell
import com.constant.everydayjapanese.scene.main.StudyActivity.OnSelectItemListener
import com.constant.everydayjapanese.singleton.JSONManager
import com.constant.everydayjapanese.singleton.Pref
import com.constant.everydayjapanese.singleton.PrefManager
import com.constant.everydayjapanese.util.HHLog
import com.constant.everydayjapanese.util.HHStyle
import com.constant.everydayjapanese.util.IndexEnum
import com.constant.everydayjapanese.util.SectionEnum
import com.constant.everydayjapanese.util.nonNull
import com.constant.everydayjapanese.view.ColorDivider
import com.constant.everydayjapanese.view.NavigationView
import io.reactivex.rxjava3.disposables.CompositeDisposable

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class TestResultActivity : AppCompatActivity() {
    // Public Inner Class, Struct, Enum, Interface

    data class Param(
        var indexEnum: IndexEnum,
        var dayTitle: String,
        var dayKey: String,
        var allCount: Int = 0,
        var kanjis: List<Kanji>?,
        var vocabularies: List<Vocabulary>?,
    )

    // companion object
    companion object {
        public val EXTRA_INDEX_ENUM = "EXTRA_INDEX_ENUM"
        public val EXTRA_DAY_TITLE = "EXTRA_DAY_TITLE"
        public val EXTRA_DAY_KEY = "EXTRA_DAY_KEY"
        public val EXTRA_ALL_COUNT = "EXTRA_ALL_COUNT"
        public val EXTRA_KANJIS = "EXTRA_KANJIS"
        public val EXTRA_VOCABULARIES = "EXTRA_VOCABULARIES"
    }

    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: ActivityTestResultBinding
    private lateinit var kanjiAdapter: KanjiAdapter
    private lateinit var vocabularyAdapter: VocabularyAdapter

    private lateinit var param: Param
    private var kanjisForCell: List<StudyActivity.KanjiForCell>? = null
    private var vocabulariesForCell: List<StudyActivity.VocabularyForCell>? = null
    private val compositeDisposable = CompositeDisposable()
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
                getIntent().getIntExtra(EXTRA_ALL_COUNT, 0),
                getIntent().getParcelableArrayListExtra<Kanji>(EXTRA_KANJIS),
                getIntent().getParcelableArrayListExtra<Vocabulary>(EXTRA_VOCABULARIES),
            )
        kanjisForCell =
            param.kanjis?.map {
                return@map StudyActivity.KanjiForCell(it)
            }
        vocabulariesForCell =
            param.vocabularies?.map {
                return@map StudyActivity.VocabularyForCell(it)
            }
    }

    private fun initializeViews() {
        binding = ActivityTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            navigationview.set(
                nonNull(param.indexEnum.getSection()?.title),
                String.format("%s %s %s", nonNull(param.indexEnum.title), param.dayTitle, getString(R.string.test_result)),
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

            buttonBookmark.setOnClickListener {
                when (param.indexEnum.getSection()) {
                    SectionEnum.kanji -> {
                        kanjiAdapter.addBookmarkAll()
                    }
                    SectionEnum.vocabulary -> {
                        vocabularyAdapter.addBookmarkAll()
                    }
                    else -> {
                        HHLog.d(TAG, "do nothing!")
                    }
                }
            }

            buttonRetest.setOnClickListener {
                finish()
                val intent = Intent(this@TestResultActivity, TestActivity::class.java)
                intent.putExtra(TestActivity.EXTRA_INDEX_ENUM, param.indexEnum.id)
                intent.putExtra(TestActivity.EXTRA_DAY_TITLE, param.dayTitle)
                intent.putExtra(TestActivity.EXTRA_DAY_KEY, param.dayKey)
                when (param.indexEnum.getSection()) {
                    SectionEnum.kanji -> {
                        param.kanjis?.let { kanjis ->
                            intent.putExtra(TestActivity.EXTRA_KANJIS, ArrayList(kanjis))
                        }
                    }
                    SectionEnum.vocabulary -> {
                        param.vocabularies?.let { vocabularies ->
                            intent.putExtra(TestActivity.EXTRA_VOCABULARIES, ArrayList(vocabularies))
                        }
                    }
                    else -> {
                        HHLog.d(TAG, "do nothing!")
                    }
                }
                startActivity(intent)
            }

            buttonFinishtest.setOnClickListener {
                finish()
            }

            if (param.indexEnum.getSection() == SectionEnum.kanji || 0 < nonNull(kanjisForCell?.size)) {
                kanjisForCell?.let { kanjisForCell ->
                    kanjiAdapter = KanjiAdapter(this@TestResultActivity, kanjisForCell, kanjiBookmarks)
                    kanjiAdapter.setOnSelectItemListener(
                        object :
                            OnSelectItemListener {
                            override fun onSelectItem(position: Int) {
                                kanjisForCell.get(position).isVisible =
                                    !kanjisForCell.get(position).isVisible
                                kanjiAdapter.notifyItemChanged(position)
                            }
                        },
                    )
                    recyclerview.adapter = kanjiAdapter
                    initializeScoreView(nonNull(kanjisForCell.size))
                }
            } else if (param.indexEnum.getSection() == SectionEnum.vocabulary || 0 < nonNull(vocabulariesForCell?.size)) { // vocabulary
                vocabulariesForCell?.let { vocabulariesForCell ->
                    vocabularyAdapter = VocabularyAdapter(this@TestResultActivity, vocabulariesForCell, vocabularyBookmarks)
                    vocabularyAdapter.setOnSelectItemListener(
                        object :
                            OnSelectItemListener {
                            override fun onSelectItem(position: Int) {
                                vocabulariesForCell.get(position).isVisible =
                                    !vocabulariesForCell.get(position).isVisible
                                vocabularyAdapter.notifyItemChanged(position)
                            }
                        },
                    )
                    recyclerview.adapter = vocabularyAdapter
                    initializeScoreView(nonNull(vocabulariesForCell.size))
                }
            }
            recyclerview.addItemDecoration(
                ColorDivider(1f, 10f, this@TestResultActivity.getColor(R.color.bg4)) {
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

    private fun initializeScoreView(wrongCount: Int) {
        binding.textviewScore.text = String.format(getString(R.string.n_points), (param.allCount - wrongCount) * 100 / param.allCount)
        binding.textviewSubscore.text = String.format("%d/%d", param.allCount - wrongCount, param.allCount)
        if (wrongCount == 0) {
            binding.buttonBookmark.isEnabled = false
            binding.buttonRetest.isEnabled = false
            if (param.indexEnum == IndexEnum.kanjiBookmark || param.indexEnum == IndexEnum.vocabularyBookmark) {
                return
            }

            saveProcess()
        }
    }

    private fun saveProcess() {
        val jsonData = JSONManager.getInstance().convertStringToByteArray(nonNull(PrefManager.getInstance().getStringValue(Pref.process.name))) ?: return
        var process = JSONManager.getInstance().decodeProcessJSON(jsonData)
        val indexKey = param.indexEnum.name
        if (process[indexKey] == null) {
            process.put(indexKey, HashMap<String, Boolean>())
        }
        process[indexKey]?.put(param.dayKey, true)
        PrefManager.getInstance().setValue(Pref.process.name, JSONManager.getInstance().encodeProcessJSON(process))
    }
}
