package com.constant.everydayjapanese.scene.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.constant.everydayjapanese.R
import com.constant.everydayjapanese.model.Vocabulary
import com.constant.everydayjapanese.scene.main.StudyActivity.OnSelectItemListener
import com.constant.everydayjapanese.scene.main.StudyActivity.VocabularyForCell
import com.constant.everydayjapanese.singleton.GlobalVariable
import com.constant.everydayjapanese.singleton.JSONManager
import com.constant.everydayjapanese.singleton.Pref
import com.constant.everydayjapanese.singleton.PrefManager
import com.constant.everydayjapanese.singleton.TTSManager
import com.constant.everydayjapanese.util.HHLog
import com.constant.everydayjapanese.util.nonNull
import io.reactivex.rxjava3.disposables.CompositeDisposable

class VocabularyAdapter(
    private val context: Context,
    private var vocabulariesForCell: List<VocabularyForCell>,
    private var vocabularyBookmarks: HashSet<Vocabulary>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textviewSound: TextView = itemView.findViewById(R.id.textview_sound)
        private val textviewWord: TextView = itemView.findViewById(R.id.textview_word)
        private val textviewMeaning: TextView = itemView.findViewById(R.id.textview_meaning)
        private val buttonBookmark: ImageButton = itemView.findViewById(R.id.button_bookmark)
        private val buttonSound: ImageButton = itemView.findViewById(R.id.button_sound)
        private val buttonExpand: ImageButton = itemView.findViewById(R.id.button_expand)
        private val linearlayoutExample: LinearLayout = itemView.findViewById(R.id.linearlayout_example)
        private val textViewExample: TextView = itemView.findViewById(R.id.textview_example)
        private val textViewTrans: TextView = itemView.findViewById(R.id.textview_trans)

        fun bind(position: Int) {
            val vocabularyForCell = vocabulariesForCell.get(position)
            textviewSound.text = vocabularyForCell.vocabulary.sound
            textviewWord.text = vocabularyForCell.vocabulary.word
            textviewMeaning.text = vocabularyForCell.vocabulary.meaning

            if (vocabularyForCell.isBookmark) {
                buttonBookmark.setImageResource(R.drawable.img_favorite_on)
            } else {
                buttonBookmark.setImageResource(R.drawable.img_favorite_off)
            }
            buttonBookmark.setOnClickListener {
                if (vocabularyForCell.isBookmark) {
                    vocabularyBookmarks.remove(vocabularyForCell.vocabulary)
                } else {
                    vocabularyBookmarks.add(vocabularyForCell.vocabulary)
                }
                vocabularyForCell.isBookmark = !vocabularyForCell.isBookmark
                notifyItemChanged(position)
                PrefManager.getInstance().setValue(
                    Pref.vocabularyBookmark.name,
                    JSONManager.getInstance().encodeVocabularyJSON(vocabularyBookmarks),
                )
            }
            buttonSound.setOnClickListener {
                TTSManager.getInstance().speak(vocabularyForCell.vocabulary.word)
            }
            if (vocabularyForCell.isExpanded) {
                buttonExpand.setImageResource(R.drawable.img_up)
            } else {
                buttonExpand.setImageResource(R.drawable.img_down)
            }
            buttonExpand.setOnClickListener {
                if (vocabularyForCell.isExpanded) {
                    vocabularyForCell.isExpanded = !vocabularyForCell.isExpanded
                    notifyItemChanged(position)
                } else {
                    if (vocabularyForCell.exampleText == null) {
//                                vocabularyForCell.exampleText = "<ruby>Android<rt>アンドロイド</rt></ruby>は、<ruby>Google<rt>グーグル</rt></ruby>が<ruby>開発<rt>かいはつ</rt></ruby>した<ruby>携帯汎用<rt>けいたいはんよう</rt></ruby>안녕"
//                                vocabularyForCell.isExpanded = !vocabularyForCell.isExpanded
//                                vocabularyAdapter.notifyItemChanged(position)
                        GlobalVariable.getInstance().tatoebaRepository.getSentence(context, vocabularyForCell.vocabulary.word)
                            .subscribe({ sentenceModel ->
                                HHLog.d(TAG, "sentenceModel.html = ${sentenceModel.html}")
                                // vocabularyForCell.exampleText = sentenceModel.html.replace("<rp>（</rp>", "").replace("<rp>）</rp>", "")
                                vocabularyForCell.exampleText = sentenceModel.text
                                vocabularyForCell.transText = sentenceModel.trans
                                vocabularyForCell.isExpanded = !vocabularyForCell.isExpanded
                                notifyItemChanged(position)
                            }, {
                            }).let { compositeDisposable.add(it) }
                    } else {
                        vocabularyForCell.isExpanded = !vocabularyForCell.isExpanded
                        notifyItemChanged(position)
                    }
                }
            }
            vocabularyForCell.exampleText?.let {
                // textViewExample.setFuriganaText(it, true)
                textViewExample.text = it
            }
            vocabularyForCell.transText?.let {
                textViewTrans.text = it
            }

            if (vocabularyForCell.isVisible || isAllVisible) {
                textviewSound.visibility = View.VISIBLE
                textviewMeaning.visibility = View.VISIBLE
            } else {
                textviewSound.visibility = View.INVISIBLE
                textviewMeaning.visibility = View.INVISIBLE
            }
            if (vocabularyForCell.isExpanded) {
                linearlayoutExample.visibility = View.VISIBLE
            } else {
                linearlayoutExample.visibility = View.GONE
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
    private val TAG = nonNull(this::class.simpleName)
    private val item: Int = 0
    private var isAllVisible: Boolean = false

    // Public Variable

    // Private Variable
    private val compositeDisposable = CompositeDisposable()
    private var onSelectItemListener: OnSelectItemListener? = null

    // Override Method or Basic Method
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_vocabulary, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = vocabulariesForCell.size

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

    fun toggleAllVisible() {
        isAllVisible = !isAllVisible
        notifyDataSetChanged()
    }

    fun shuffle() {
        vocabulariesForCell = vocabulariesForCell.shuffled()
        notifyDataSetChanged()
    }

    fun addBookmarkAll() {
        vocabulariesForCell.forEach { vocabularyForCell ->
            if (!vocabularyForCell.isBookmark) {
                vocabularyBookmarks.add(vocabularyForCell.vocabulary)
                vocabularyForCell.isBookmark = true
            }
        }
        notifyDataSetChanged()
        PrefManager.getInstance().setValue(
            Pref.vocabularyBookmark.name,
            JSONManager.getInstance().encodeVocabularyJSON(vocabularyBookmarks),
        )
    }
}
