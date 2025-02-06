package com.constant.japaneseeveryday.scene.common

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.databinding.ActivityListExBinding
import com.constant.japaneseeveryday.util.nonNull

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
open class ListExActivity : AppCompatActivity() {
    // Public Inner Class, Struct, Enum, Interface
    data class ListExData(
        val title: String,
    )

    interface OnSelectItemListener {
        fun onSelectItem(itemText: String)
    }

    inner class ListExAdapter(private val context: Context) : RecyclerView.Adapter<ListExAdapter.ViewHolder>() {
        private var onSelectItemListener: OnSelectItemListener? = null
        private var arrayList: ArrayList<ListExData> = ArrayList<ListExData>()

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int,
        ): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.list_title, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int = nonNull(arrayList.size)

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int,
        ) {
            holder.bind(arrayList[position] ?: ListExData("none"))
        }

        fun setOnSelectItemListener(onSelectItemListener: OnSelectItemListener) {
            this.onSelectItemListener = onSelectItemListener
        }

        fun setArrayList(arrayList: ArrayList<ListExData>) {
            this.arrayList = arrayList
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val textviewTitle: TextView = itemView.findViewById(R.id.textview_title)
            private val imageviewDisclosure: ImageView = itemView.findViewById(R.id.imageview_disclosure)
            private val viewSeparator: View = itemView.findViewById(R.id.view_seperator)

            fun bind(item: ListExData) {
                textviewTitle.text = item.title
                imageviewDisclosure.visibility = View.GONE
                viewSeparator.visibility = View.GONE
                itemView.setOnClickListener {
                    onSelectItemListener?.onSelectItem(item.title)
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
    }
    // companion object
    // Public Constant

    // Private Constant
    private lateinit var binding: ActivityListExBinding

    // Public Variable
    lateinit var adapter: ListExAdapter

    // Private Variable
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
        binding = ActivityListExBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            recyclerview.addItemDecoration(DividerItemDecoration(this@ListExActivity, LinearLayoutManager.VERTICAL))
            adapter = ListExAdapter(this@ListExActivity)
            recyclerview.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}
