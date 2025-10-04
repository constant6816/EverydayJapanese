package com.constant.everydayjapanese.scene.main

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.databinding.ActivityMainBinding
import com.constant.everydayjapanese.model.Kanji
import com.constant.everydayjapanese.model.Vocabulary
import com.constant.everydayjapanese.scene.debug.SceneActivity
import com.constant.everydayjapanese.singleton.JSONManager
import com.constant.everydayjapanese.singleton.Pref
import com.constant.everydayjapanese.singleton.PrefManager
import com.constant.everydayjapanese.util.FeatureConst
import com.constant.everydayjapanese.util.HHLog
import com.constant.everydayjapanese.util.IndexEnum
import com.constant.everydayjapanese.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class MainActivity : AppCompatActivity() {
    // ----------------------------------------------------
    // Public Inner Class, Struct, Enum, Interface
    interface OnSelectItemListener {
        fun onSelectItem(position: Int)
    }

    inner class ChartAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Public Inner Class, Struct, Enum, Interface
        inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val imageviewIcon: ImageView = itemView.findViewById(R.id.imageview_icon)
            private val textviewTitle: TextView = itemView.findViewById(R.id.textview_title)

            fun bind() {
                imageviewIcon.setImageResource(R.drawable.hiraganakatakana)
                textviewTitle.text = getString(R.string.hiragana_katakana)
            }
        }

        inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val imageviewIcon: ImageView = itemView.findViewById(R.id.imageview_icon)
            private val textviewTitle: TextView = itemView.findViewById(R.id.textview_title)
            private val imageviewDisclosure: ImageView = itemView.findViewById(R.id.imageview_disclosure)

            fun bind(position: Int) {
                val chart = charts.get(position)
                imageviewIcon.setImageResource(chart.resourceId)
                textviewTitle.text = chart.indexEnum.title
                imageviewDisclosure.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.fg5))

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
        private val title: Int = 0
        private val item: Int = 1

        // Public Variable

        // Private Variable
        private var onSelectItemListener: OnSelectItemListener? = null

        // Override Method or Basic Method
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RecyclerView.ViewHolder {
            return if (viewType == title) {
                val view = LayoutInflater.from(context).inflate(R.layout.list_icon_title, parent, false)
                TitleViewHolder(view)
            } else {
                val view = LayoutInflater.from(context).inflate(R.layout.list_icon_title_disclosure, parent, false)
                ItemViewHolder(view)
            }
        }

        override fun getItemCount(): Int = charts.size + 1

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) {
                title
            } else {
                item
            }
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            if (getItemViewType(position) == title) {
                val titleViewHolder: TitleViewHolder = holder as TitleViewHolder
                titleViewHolder.bind()
            } else {
                val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
                itemViewHolder.bind(position - 1)
            }
        }

        // Public Method
        fun setOnSelectItemListener(onSelectItemListener: OnSelectItemListener) {
            this.onSelectItemListener = onSelectItemListener
        }
    } // End of ChartAdapter

    inner class CommonKanjiAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Public Inner Class, Struct, Enum, Interface
        inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val imageviewIcon: ImageView = itemView.findViewById(R.id.imageview_icon)
            private val textviewTitle: TextView = itemView.findViewById(R.id.textview_title)

            fun bind() {
                imageviewIcon.setImageResource(R.drawable.kanji)
                textviewTitle.text = getString(R.string.japanese_common_chinese)
            }
        }

        inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val imageviewIcon: ImageView = itemView.findViewById(R.id.imageview_icon)
            private val textviewTitle: TextView = itemView.findViewById(R.id.textview_title)
            private val imageviewDisclosure: ImageView = itemView.findViewById(R.id.imageview_disclosure)

            fun bind(position: Int) {
                val commonKanji = commonKanjis.get(position)
                if (position == 0) {
                    imageviewIcon.setImageResource(R.drawable.star)
                    ImageViewCompat.setImageTintList(imageviewIcon, null)
                } else {
                    if (process[commonKanji.indexEnum.name]?.get(getString(R.string.all_view)) == true) {
                        imageviewIcon.setImageResource(R.drawable.img_check)
                        ImageViewCompat.setImageTintList(
                            imageviewIcon,
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.fg_green)),
                        )
                    } else {
                        imageviewIcon.setImageResource(R.drawable.img_uncheck)
                        ImageViewCompat.setImageTintList(
                            imageviewIcon,
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.fg0)),
                        )
                    }
                }

                textviewTitle.text = commonKanji.indexEnum.title
                imageviewDisclosure.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.fg5))

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
        private val title: Int = 0
        private val item: Int = 1

        // Public Variable

        // Private Variable
        private var onSelectItemListener: OnSelectItemListener? = null

        // Override Method or Basic Method
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RecyclerView.ViewHolder {
            return if (viewType == title) {
                val view = LayoutInflater.from(context).inflate(R.layout.list_icon_title, parent, false)
                TitleViewHolder(view)
            } else {
                val view = LayoutInflater.from(context).inflate(R.layout.list_icon_title_disclosure, parent, false)
                ItemViewHolder(view)
            }
        }

        override fun getItemCount(): Int = commonKanjis.size + 1

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) {
                title
            } else {
                item
            }
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            if (getItemViewType(position) == title) {
                val titleViewHolder: TitleViewHolder = holder as TitleViewHolder
                titleViewHolder.bind()
            } else {
                val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
                itemViewHolder.bind(position - 1)
            }
        }

        // Public Method
        fun setOnSelectItemListener(onSelectItemListener: OnSelectItemListener) {
            this.onSelectItemListener = onSelectItemListener
        }
    } // End of CommonKanjiAdapter

    inner class VocabularyAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // Public Inner Class, Struct, Enum, Interface
        inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val imageviewIcon: ImageView = itemView.findViewById(R.id.imageview_icon)
            private val textviewTitle: TextView = itemView.findViewById(R.id.textview_title)

            fun bind() {
                imageviewIcon.setImageResource(R.drawable.jlpt)
                textviewTitle.text = getString(R.string.jlpt_vocabulary)
            }
        }

        inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val imageviewIcon: ImageView = itemView.findViewById(R.id.imageview_icon)
            private val textviewTitle: TextView = itemView.findViewById(R.id.textview_title)
            private val imageviewDisclosure: ImageView = itemView.findViewById(R.id.imageview_disclosure)

            fun bind(position: Int) {
                val vocabulary = vocabularies.get(position)
                if (position == 0) {
                    imageviewIcon.setImageResource(R.drawable.star)
                    ImageViewCompat.setImageTintList(imageviewIcon, null)
                } else {
                    if (process[vocabulary.indexEnum.name]?.get(getString(R.string.all_view)) == true) {
                        imageviewIcon.setImageResource(R.drawable.img_check)
                        ImageViewCompat.setImageTintList(
                            imageviewIcon,
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.fg_green)),
                        )
                    } else {
                        imageviewIcon.setImageResource(R.drawable.img_uncheck)
                        ImageViewCompat.setImageTintList(
                            imageviewIcon,
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.fg0)),
                        )
                    }
                }

                textviewTitle.text = vocabulary.indexEnum.title
                imageviewDisclosure.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.fg5))

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
        private val title: Int = 0
        private val item: Int = 1

        // Public Variable

        // Private Variable
        private var onSelectItemListener: OnSelectItemListener? = null

        // Override Method or Basic Method
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RecyclerView.ViewHolder {
            return if (viewType == title) {
                val view = LayoutInflater.from(context).inflate(R.layout.list_icon_title, parent, false)
                TitleViewHolder(view)
            } else {
                val view = LayoutInflater.from(context).inflate(R.layout.list_icon_title_disclosure, parent, false)
                ItemViewHolder(view)
            }
        }

        override fun getItemCount(): Int = vocabularies.size + 1

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) {
                title
            } else {
                item
            }
        }

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            if (getItemViewType(position) == title) {
                val titleViewHolder: TitleViewHolder = holder as TitleViewHolder
                titleViewHolder.bind()
            } else {
                val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
                itemViewHolder.bind(position - 1)
            }
        }

        // Public Method
        fun setOnSelectItemListener(onSelectItemListener: OnSelectItemListener) {
            this.onSelectItemListener = onSelectItemListener
        }
    } // End of VocabularyAdapter

    data class Item(var resourceId: Int, var indexEnum: IndexEnum)

    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: ActivityMainBinding
    private lateinit var process: HashMap<String, HashMap<String, Boolean>>

    private var charts = ArrayList<Item>()
    private lateinit var chartAdapter: ChartAdapter

    private var commonKanjis = ArrayList<Item>()
    private lateinit var commonKanjiAdapter: CommonKanjiAdapter

    private var vocabularies = ArrayList<Item>()
    private lateinit var vocabularyAdapter: VocabularyAdapter

    // ----------------------------------------------------
    // Override Method or Basic Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeVariables()
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        HHLog.d(TAG, "onResume()")
        val processJsonData =
            JSONManager.getInstance().convertStringToByteArray(
                nonNull(PrefManager.getInstance().getStringValue(Pref.process.name)),
            )
        processJsonData?.let { processJsonData ->
            process = JSONManager.getInstance().decodeProcessJSON(processJsonData)
        }
        commonKanjiAdapter.notifyDataSetChanged()
        vocabularyAdapter.notifyDataSetChanged()
    }

    // Public Method
    // Private Method

    private fun initializeVariables() {
        charts.add(Item(R.drawable.hiragana_hi, IndexEnum.hiragana))
        charts.add(Item(R.drawable.katakana_ka, IndexEnum.katakana))

        commonKanjis.add(Item(R.drawable.star, IndexEnum.kanjiBookmark))
        commonKanjis.add(Item(R.drawable.img_uncheck, IndexEnum.elementary1))
        commonKanjis.add(Item(R.drawable.img_uncheck, IndexEnum.elementary2))
        commonKanjis.add(Item(R.drawable.img_uncheck, IndexEnum.elementary3))
        commonKanjis.add(Item(R.drawable.img_uncheck, IndexEnum.elementary4))
        commonKanjis.add(Item(R.drawable.img_uncheck, IndexEnum.elementary5))
        commonKanjis.add(Item(R.drawable.img_uncheck, IndexEnum.elementary6))
        commonKanjis.add(Item(R.drawable.img_uncheck, IndexEnum.middle))

        vocabularies.add(Item(R.drawable.star, IndexEnum.vocabularyBookmark))
        vocabularies.add(Item(R.drawable.img_uncheck, IndexEnum.n5))
        vocabularies.add(Item(R.drawable.img_uncheck, IndexEnum.n4))
        vocabularies.add(Item(R.drawable.img_uncheck, IndexEnum.n3))
        vocabularies.add(Item(R.drawable.img_uncheck, IndexEnum.n2))
        vocabularies.add(Item(R.drawable.img_uncheck, IndexEnum.n1))
    }

    private fun initializeViews() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            chartAdapter = ChartAdapter(this@MainActivity)
            chartAdapter.setOnSelectItemListener(
                object : OnSelectItemListener {
                    override fun onSelectItem(position: Int) {
                        HHLog.d(TAG, "onSelectItem() position = $position")
                        val intent = Intent(this@MainActivity, HiraganaKatakanaActivity::class.java)
                        intent.putExtra(
                            HiraganaKatakanaActivity.EXTRA_INDEX_ENUM,
                            charts[position].indexEnum.id,
                        )
                        startActivity(intent)
                    }
                },
            )
            recyclerviewChart.adapter = chartAdapter

            commonKanjiAdapter = CommonKanjiAdapter(this@MainActivity)
            commonKanjiAdapter.setOnSelectItemListener(
                object : OnSelectItemListener {
                    override fun onSelectItem(position: Int) {
                        HHLog.d(TAG, "onSelectItem() position = $position")
                        if (position == 0) { // 즐겨찾기
                            val intent = Intent(this@MainActivity, StudyActivity::class.java)
                            var bookmarks: ArrayList<Kanji>
                            PrefManager.getInstance().getStringValue(
                                Pref.kanjiBookmark.name,
                            )?.let {
                                bookmarks = JSONManager.getInstance().decodeJSONtoKanjiArray(it) as ArrayList<Kanji>
                                intent.putExtra(StudyActivity.EXTRA_KANJIS_DAY_DISTRIBUTED, bookmarks)
                            }
                            intent.putExtra(StudyActivity.EXTRA_INDEX_ENUM, IndexEnum.kanjiBookmark.id)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@MainActivity, DayActivity::class.java)
                            intent.putExtra(
                                DayActivity.EXTRA_INDEX_ENUM,
                                commonKanjis[position].indexEnum.id,
                            )
                            startActivity(intent)
                        }
                    }
                },
            )
            recyclerviewCommonKanji.adapter = commonKanjiAdapter

            vocabularyAdapter = VocabularyAdapter(this@MainActivity)
            vocabularyAdapter.setOnSelectItemListener(
                object : OnSelectItemListener {
                    override fun onSelectItem(position: Int) {
                        HHLog.d(TAG, "onSelectItem() position = $position")
                        if (position == 0) { // 즐겨찾기
                            val intent = Intent(this@MainActivity, StudyActivity::class.java)
                            var bookmarks: ArrayList<Vocabulary>
                            PrefManager.getInstance().getStringValue(
                                Pref.vocabularyBookmark.name,
                            )?.let {
                                bookmarks = JSONManager.getInstance().decodeJSONtoVocabularyArray(it) as ArrayList<Vocabulary>
                                intent.putExtra(StudyActivity.EXTRA_VOCABULARIES_DAY_DISTRIBUTED, bookmarks)
                            }
                            intent.putExtra(StudyActivity.EXTRA_INDEX_ENUM, IndexEnum.vocabularyBookmark.id)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@MainActivity, DayActivity::class.java)
                            intent.putExtra(
                                DayActivity.EXTRA_INDEX_ENUM,
                                vocabularies[position].indexEnum.id,
                            )
                            startActivity(intent)
                        }
                    }
                },
            )
            recyclerviewVocabulary.adapter = vocabularyAdapter
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }

        if (FeatureConst.FEATURE_SPECIFIC_SCENE) {
            val intent = Intent(this@MainActivity, SceneActivity::class.java)
            startActivity(intent)
        }
    }
}
