package com.constant.japaneseeveryday.scene.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.constant.japaneseeveryday.R
import com.constant.japaneseeveryday.databinding.BottomsheetHhActionSheetBinding
import com.constant.japaneseeveryday.util.ColorUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
data class HHActionSheetAction(var title: String, var image: Drawable? = null, var handler: View.OnClickListener? = null)

class HHActionSheetAdapter : RecyclerView.Adapter<HHActionSheetAdapter.Holder>() {
    private var actions: MutableList<HHActionSheetAction> = ArrayList()
    private lateinit var context:Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): Holder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_icon_title_description, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return actions.size
    }

    override fun onBindViewHolder(
        holder: Holder,
        position: Int,
    ) {
        val item = actions[position]
        holder.bind(item)
    }

    fun setActions(actions: MutableList<HHActionSheetAction>) {
        if (!actions.isNullOrEmpty()) {
            this.actions = actions
            notifyDataSetChanged()
        }
    }

    inner class Holder(val view: View) : RecyclerView.ViewHolder(view) {
        private val imageviewIcon: ImageView = itemView.findViewById(R.id.imageview_icon)
        private val textviewTitle: TextView = itemView.findViewById(R.id.textview_title)
        private val textviewDetail: TextView = itemView.findViewById(R.id.textview_detail)

        fun bind(action: HHActionSheetAction) {
            if (action.image == null) {
                imageviewIcon.visibility = View.GONE
            } else {
                imageviewIcon.setImageDrawable(action.image)
            }
            textviewTitle.text = action.title
            textviewDetail.visibility = View.GONE
            itemView.setOnClickListener {
                // 여기서 dismiss 할 방법이 없나???? 그러면 너무 dependency 가 많아지네...

                action.handler?.onClick(it)
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

class HHActionSheetFragment(var adapter: HHActionSheetAdapter) : BottomSheetDialogFragment() {
    // Public Inner Class, Struct, Enum, Interface

    // companion object
    // Public Constant
    // Private Constant
    // Public Variable
    // Private Variable
    private lateinit var binding: BottomsheetHhActionSheetBinding

    // Override Method or Basic Method
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        initializeVariables()
        initializeViews(inflater, container)
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.ActionSheetDialog
    }

    // Public Method
    // Private Method
    private fun initializeVariables() {
    }

    private fun initializeViews(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) {
        binding =
            DataBindingUtil.inflate<BottomsheetHhActionSheetBinding?>(inflater, R.layout.bottomsheet_hh_action_sheet, container, false).apply {
                recyclerview.adapter = adapter
                // recyclerview.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            }
    }
}
