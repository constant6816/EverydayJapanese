package com.constant.everydayjapanese.scene.main

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.databinding.ActivitySettingBinding
import com.constant.everydayjapanese.scene.common.HHActionSheetAction
import com.constant.everydayjapanese.scene.common.HHActionSheetAdapter
import com.constant.everydayjapanese.scene.common.HHActionSheetFragment
import com.constant.everydayjapanese.scene.main.MainActivity.Item
import com.constant.everydayjapanese.singleton.Pref
import com.constant.everydayjapanese.singleton.PrefManager
import com.constant.everydayjapanese.util.FrequencyEnum
import com.constant.everydayjapanese.util.HHLog
import com.constant.everydayjapanese.util.HHStyle
import com.constant.everydayjapanese.util.IndexEnum
import com.constant.everydayjapanese.util.nonNull
import com.constant.everydayjapanese.view.NavigationView
import com.constant.everydayjapanese.widget.EverydayJapaneseAppWidget


// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class SettingActivity : AppCompatActivity() {
    // Public Inner Class, Struct, Enum, Interface
    enum class MenuIndex {
        PART_TO_MEMORIZE,
        FREQUENCE_OF_WORD_CHANGE,
    }
    enum class ViewType(val id: Int) {
        TITLE(0),
    }

    interface OnSelectItemListener {
        fun onSelectItem(id: MenuIndex)
    }

    data class Item(
        var id: MenuIndex,
        var title: String?,
        var description: String?,
    )

    inner class ListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val tvTitle: TextView = itemView.findViewById(R.id.textview_title)
            private val tvDescription: TextView = itemView.findViewById(R.id.textview_description)
            private val ivDisclosure: ImageView = itemView.findViewById(R.id.imageview_disclosure)
            private val vSeperator: View = itemView.findViewById(R.id.view_seperator)

            fun bind(item: Item) {
                tvTitle.text = item.title
                tvDescription.text = item.description
                ivDisclosure.visibility = View.VISIBLE
                vSeperator.visibility = View.VISIBLE

                itemView.setOnClickListener {
                    onSelectItemListener?.onSelectItem(item.id!!)
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

        private var onSelectItemListener: OnSelectItemListener? = null
        private var arrayList: ArrayList<Item> = ArrayList<Item>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.list_title_description, parent, false)
            return TitleViewHolder(view)
        }

        override fun getItemCount(): Int = nonNull(arrayList.size)

        override fun onBindViewHolder(
            holder: RecyclerView.ViewHolder,
            position: Int,
        ) {
            val titleViewHolder = holder as TitleViewHolder
            titleViewHolder.bind(arrayList[position])
        }

        override fun getItemViewType(position: Int): Int {
            return ViewType.TITLE.id
        }

        fun setOnSelectItemListener(onSelectItemListener: OnSelectItemListener) {
            this.onSelectItemListener = onSelectItemListener
        }

        fun setArrayList(arrayList: ArrayList<Item>) {
            this.arrayList = arrayList
        }
    }

    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)

    // Public Variable
    // Private Variable
    private lateinit var binding: ActivitySettingBinding
    private lateinit var adapter: ListAdapter

    private var items = ArrayList<Item>()

    private var memoryLists: ArrayList<IndexEnum> = arrayListOf(
        IndexEnum.kanjiBookmark,
        IndexEnum.elementary1,
        IndexEnum.elementary2,
        IndexEnum.elementary3,
        IndexEnum.elementary4,
        IndexEnum.elementary5,
        IndexEnum.elementary6,
        IndexEnum.middle,
        IndexEnum.vocabularyBookmark,
        IndexEnum.n5,
        IndexEnum.n4,
        IndexEnum.n3,
        IndexEnum.n2,
        IndexEnum.n1
    )

    private var frequencyLists: ArrayList<FrequencyEnum> = arrayListOf(
        FrequencyEnum.day,
        FrequencyEnum.hour
    )


    // Override Method or Basic Method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initializeVariables()
        initializeViews()
    }

    override fun onPause() {
        super.onPause()
        notifyWidgetUpdate()
    }

    // Public Method
    // Private Method
    private fun initializeVariables() {
        items.clear()
        items.add(
            Item(
                MenuIndex.PART_TO_MEMORIZE,
                getString(R.string.part_memorize),
                IndexEnum.ofRaw(PrefManager.getInstance().getIntValue(Pref.partToMemorize.name)).getSettingTitle(),
            ),
        )
        items.add(
            Item(
                MenuIndex.FREQUENCE_OF_WORD_CHANGE,
                getString(R.string.frequency_word_change),
                FrequencyEnum.ofRaw(PrefManager.getInstance().getIntValue(Pref.frequenceOfWordChange.name)).title,
            ),
        )
    }

    private fun initializeViews() {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            //navigationview.setTitle(getString(R.string.tab_setting))
            navigationview.setButtonStyle(
                HHStyle(
                    NavigationView.ButtonId.leftBack,
                ),
            )
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
            navigationview.setStyle(HHStyle(NavigationView.Style.onlyTitle))
            navigationview.set(getString(R.string.common_setting), "", 0)
            adapter = ListAdapter(baseContext)
            adapter.setArrayList(items)
            adapter.setOnSelectItemListener(
                object : OnSelectItemListener {
                    override fun onSelectItem(id: MenuIndex) {
                        when(id) {
                            MenuIndex.PART_TO_MEMORIZE -> {
                                HHLog.d(TAG, "PART_TO_MEMORIZE")
                                val adapter = HHActionSheetAdapter()
                                val actionSheetFragment = HHActionSheetFragment(adapter)
                                val actions = ArrayList<HHActionSheetAction>()
                                memoryLists.forEach { indexEnum ->
                                    actions.add(
                                        HHActionSheetAction(
                                            indexEnum.getSettingTitle(),
                                            null,
                                            object : View.OnClickListener {
                                                override fun onClick(view: View) {
                                                    actionSheetFragment.dialog?.dismiss()
                                                    PrefManager.getInstance().setValue(Pref.partToMemorize.name, indexEnum.id)
                                                    initializeVariables()
                                                    this@SettingActivity.adapter.notifyDataSetChanged()
                                                }
                                            },
                                        ),
                                    )
                                }
                                adapter.setActions(actions)

                                actionSheetFragment.dialog?.getWindow()?.setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                                actionSheetFragment.show(supportFragmentManager, nonNull(this::class.simpleName))
                            }
                            MenuIndex.FREQUENCE_OF_WORD_CHANGE -> {
                                HHLog.d(TAG, "FREQUENCE_OF_WORD_CHANGE")
                                val adapter = HHActionSheetAdapter()
                                val actionSheetFragment = HHActionSheetFragment(adapter)
                                val actions = ArrayList<HHActionSheetAction>()
                                frequencyLists.forEach { frequencyEnum ->
                                    actions.add(
                                        HHActionSheetAction(
                                            frequencyEnum.title,
                                            null,
                                            object : View.OnClickListener {
                                                override fun onClick(view: View) {
                                                    actionSheetFragment.dialog?.dismiss()
                                                    PrefManager.getInstance().setValue(Pref.frequenceOfWordChange.name, frequencyEnum.id)
                                                    initializeVariables()
                                                    this@SettingActivity.adapter.notifyDataSetChanged()
                                                }
                                            },
                                        ),
                                    )
                                }

                                adapter.setActions(actions)
                                actionSheetFragment.dialog?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                                actionSheetFragment.show(supportFragmentManager, nonNull(this::class.simpleName))
                            }
                        }
                    }
                },
            )
            recyclerview.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }
    }

    private fun notifyWidgetUpdate() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(
            ComponentName(this, EverydayJapaneseAppWidget::class.java)
        )
        if (ids.isNotEmpty()) {
            val intent = Intent(this, EverydayJapaneseAppWidget::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            sendBroadcast(intent)
        }
    }
}
