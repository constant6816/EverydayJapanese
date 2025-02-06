package com.constant.japaneseeveryday.scene.main

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.databinding.ActivityDayBinding
import com.constant.japaneseeveryday.databinding.ActivityStudyBinding
import com.constant.japaneseeveryday.extension.LATER
import com.constant.japaneseeveryday.extension.applyGUI
import com.constant.japaneseeveryday.model.Kanji
import com.constant.japaneseeveryday.model.Vocabulary
import com.constant.japaneseeveryday.util.GlobalConst
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.HHStyle
import com.constant.japaneseeveryday.util.IndexEnum
import com.constant.japaneseeveryday.util.JsonParser
import com.constant.japaneseeveryday.util.SectionEnum
import com.constant.japaneseeveryday.util.nonNull
import com.constant.japaneseeveryday.view.NavigationView

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class StudyActivity : AppCompatActivity() {
    // Public Inner Class, Struct, Enum, Interface
    interface OnSelectItemListener {
        fun onSelectItem(position: Int)
    }
    inner class KanjiAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Public Inner Class, Struct, Enum, Interface

        inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val textviewSound: TextView = itemView.findViewById(R.id.textview_sound)
            private val textviewMeaning: TextView = itemView.findViewById(R.id.textview_meaning)
            private val textviewKanji: TextView = itemView.findViewById(R.id.textview_kanji)
            private val textviewEumhun: TextView = itemView.findViewById(R.id.textview_eumhun)
            private val buttonBookmark: ImageButton = itemView.findViewById(R.id.button_bookmark)
            private val buttonSound: ImageButton = itemView.findViewById(R.id.button_sound)
            private val buttonExpand: ImageButton = itemView.findViewById(R.id.button_expand)
            private val linearlayoutExample: LinearLayout = itemView.findViewById(R.id.linearlayout_example)

            fun bind(position: Int) {
                val kanjiForCell = kanjisForCell?.get(position)?.let { kanjiForCell ->
                    textviewSound.text = kanjiForCell.kanji.jpSound
                    textviewMeaning.text = kanjiForCell.kanji.jpMeaning
                    textviewKanji.text = kanjiForCell.kanji.kanji
                    textviewEumhun.text = kanjiForCell.kanji.eumhun
                    buttonBookmark.setOnClickListener {

                    }
                    buttonSound.setOnClickListener {

                    }
                    buttonExpand.setOnClickListener {
                        kanjiForCell.isExpanded = !kanjiForCell.isExpanded
                        kanjiAdapter.notifyItemChanged(position)
                    }
                    val marginHorizontal = resources.getDimensionPixelSize(R.dimen.space_m)
                    linearlayoutExample.removeAllViews()
                    kanjiForCell.kanji.examples.forEach { vocabulary ->
                        val linearLayout = LinearLayout(this@StudyActivity).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(marginHorizontal, 0, marginHorizontal, 0) // 좌우 마진 추가
                            }
                        }

                        // TextView 생성 및 추가
                        val textViewWord = TextView(this@StudyActivity).apply {
                            text = vocabulary.word
                            applyGUI(R.style.font_p2, R.color.fg0)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }
                        val view = View(this@StudyActivity).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                0,
                                0,
                                1f
                            )
                        }
                        val textViewMeaning = TextView(this@StudyActivity).apply {
                            text = vocabulary.meaning
                            applyGUI(R.style.font_p2, R.color.fg0)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }
                        linearLayout.addView(textViewWord)
                        linearLayout.addView(view)
                        linearLayout.addView(textViewMeaning)
                        linearlayoutExample.addView(linearLayout)
                    }
                    if (kanjiForCell.isVisible) {
                        textviewSound.visibility = View.VISIBLE
                        textviewMeaning.visibility = View.VISIBLE
                        textviewEumhun.visibility = View.VISIBLE
                    } else {
                        textviewSound.visibility = View.INVISIBLE
                        textviewMeaning.visibility = View.INVISIBLE
                        textviewEumhun.visibility = View.INVISIBLE
                    }
                    if (kanjiForCell.isExpanded) {
                        linearlayoutExample.visibility= View.VISIBLE
                    } else {
                        linearlayoutExample.visibility= View.GONE
                    }
                }
                itemView.setOnClickListener {
                    onSelectItemListener?.onSelectItem(position)
                }

                itemView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundColor(context.getColor(R.color.list_selection))
                    }
                    if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                        v.setBackgroundColor(Color.TRANSPARENT)
                    }
                    false
                }
            }
        }

        // Private Constant
        private val item: Int = 0

        // Public Variable

        // Private Variable
        private var onSelectItemListener: OnSelectItemListener? = null
        // Override Method or Basic Method
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.list_kanji, parent, false)
            return ItemViewHolder(view)
        }

        override fun getItemCount(): Int = nonNull(kanjisForCell?.size)

        override fun getItemViewType(position: Int): Int {
            return item
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
            itemViewHolder.bind(position)
        }

        // Public Method
        fun setOnSelectItemListener(onSelectItemListener: OnSelectItemListener) {
            this.onSelectItemListener = onSelectItemListener
        }
    } // End of KanjiAdapter





    inner class VocabularyAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Public Inner Class, Struct, Enum, Interface

        inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val textviewSound: TextView = itemView.findViewById(R.id.textview_sound)
            private val textviewWord: TextView = itemView.findViewById(R.id.textview_word)
            private val textviewMeaning: TextView = itemView.findViewById(R.id.textview_meaning)
            private val buttonBookmark: ImageButton = itemView.findViewById(R.id.button_bookmark)
            private val buttonSound: ImageButton = itemView.findViewById(R.id.button_sound)
            private val buttonExpand: ImageButton = itemView.findViewById(R.id.button_expand)

            fun bind(position: Int) {
                val vocabularyForCell = vocabulariesForCell?.get(position)?.let { vocabularyForCell ->
                    textviewSound.text = vocabularyForCell.vocabulary.sound
                    textviewWord.text = vocabularyForCell.vocabulary.word
                    textviewMeaning.text = vocabularyForCell.vocabulary.meaning
                    buttonBookmark.setOnClickListener {

                    }
                    buttonSound.setOnClickListener {

                    }
                    buttonExpand.setOnClickListener {

                    }
                    if (vocabularyForCell.isVisible) {
                        textviewSound.visibility = View.VISIBLE
                        textviewWord.visibility = View.VISIBLE
                        textviewMeaning.visibility = View.VISIBLE
                    } else {
                        textviewSound.visibility = View.INVISIBLE
                        textviewWord.visibility = View.INVISIBLE
                        textviewMeaning.visibility = View.INVISIBLE
                    }
                }

                itemView.setOnClickListener {
                    onSelectItemListener?.onSelectItem(position)
                }

                itemView.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        v.setBackgroundColor(context.getColor(R.color.list_selection))
                    }
                    if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                        v.setBackgroundColor(Color.TRANSPARENT)
                    }
                    false
                }
            }
        }

        // Private Constant
        private val item: Int = 0

        // Public Variable

        // Private Variable
        private var onSelectItemListener: OnSelectItemListener? = null
        // Override Method or Basic Method
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.list_vocabulary, parent, false)
            return ItemViewHolder(view)
        }

        override fun getItemCount(): Int = nonNull(vocabulariesForCell?.size)

        override fun getItemViewType(position: Int): Int {
            return item
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
            itemViewHolder.bind(position)
        }

        // Public Method
        fun setOnSelectItemListener(onSelectItemListener: OnSelectItemListener) {
            this.onSelectItemListener = onSelectItemListener
        }
    } // End of VocabularyAdapter
    data class Param (
        var indexEnum:IndexEnum,
        var day:Int = 0,
        var kanjisDayDistributed:List<Kanji>?,
        var vocabulariesDayDistributed:List<Vocabulary>?
    )

    data class KanjiForCell(
        var kanji: Kanji,
        var isVisible: Boolean = false,
        var isVisibleHanja: Boolean = false,
        var isBookmark: Boolean = false,
        var isExpanded: Boolean = false
    )
    data class VocabularyForCell(
        var vocabulary: Vocabulary,
        var isVisible: Boolean = false,
        var isBookmark: Boolean = false,
        var isExpanded: Boolean = false
    )
    // companion object
    companion object {
        public val EXTRA_INDEX_ENUM = "EXTRA_INDEX_ENUM"
        public val EXTRA_DAY = "EXTRA_DAY"
        public val EXTRA_KANJIS_DAY_DISTRIBUTED = "EXTRA_KANJIS_DAY_DISTRIBUTED"
        public val EXTRA_VOCABULARIES_DAY_DISTRIBUTED = "EXTRA_VOCABULARIES_DAY_DISTRIBUTED"
    }
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: ActivityStudyBinding
    private lateinit var kanjiAdapter: KanjiAdapter
    private lateinit var vocabularyAdapter: VocabularyAdapter

    // Param
    private lateinit var param: Param
    private var kanjisForCell: List<KanjiForCell>? = null
    private var vocabulariesForCell: List<VocabularyForCell>? = null

    // Override Method or Basic Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeVariables()
        initializeViews()
    }
    // Public Method
    // Private Method

    private fun initializeVariables() {
        param = Param(
            IndexEnum.ofRaw(getIntent().getIntExtra(EXTRA_INDEX_ENUM, 0)),
            getIntent().getIntExtra(EXTRA_DAY, 0),
            getIntent().getParcelableArrayListExtra<Kanji>(EXTRA_KANJIS_DAY_DISTRIBUTED),
            getIntent().getParcelableArrayListExtra<Vocabulary>(EXTRA_VOCABULARIES_DAY_DISTRIBUTED)
        )
        kanjisForCell = param.kanjisDayDistributed?.map {
            return@map KanjiForCell(it)
        }
        vocabulariesForCell = param.vocabulariesDayDistributed?.map {
            return@map VocabularyForCell(it)
        }
    }

    private fun initializeViews() {
        binding = ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            navigationview.set(nonNull(param.indexEnum.getSection()?.title), nonNull(param.indexEnum.title), param.indexEnum.getResourceId())
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

            if (param.indexEnum.getSection() == SectionEnum.kanji) {
                kanjiAdapter = KanjiAdapter(this@StudyActivity)
                kanjiAdapter.setOnSelectItemListener(
                    object : OnSelectItemListener {
                        override fun onSelectItem(position: Int) {
                            kanjisForCell?.get(position)?.isVisible = !nonNull(kanjisForCell?.get(position)?.isVisible)
                            kanjiAdapter.notifyItemChanged(position)
                        }
                    })
                recyclerview.adapter = kanjiAdapter
            } else { // vocabulary
                vocabularyAdapter = VocabularyAdapter(this@StudyActivity)
                vocabularyAdapter.setOnSelectItemListener(
                    object : OnSelectItemListener {
                        override fun onSelectItem(position: Int) {
                            vocabulariesForCell?.get(position)?.isVisible = !nonNull(vocabulariesForCell?.get(position)?.isVisible)
                            vocabularyAdapter.notifyItemChanged(position)
                        }
                    })
                recyclerview.adapter = vocabularyAdapter
            }
        }
    }
}