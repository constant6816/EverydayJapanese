package com.constant.japaneseeveryday.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    val layoutManager: LinearLayoutManager = layoutManager

    override fun onScrolled(
        recyclerView: RecyclerView,
        dx: Int,
        dy: Int,
    ) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
            if (!isPaging() && hasNextPage()) {
                beginPaging()
            }
        }
    }

    abstract fun beginPaging()

    abstract fun hasNextPage(): Boolean

    abstract fun isPaging(): Boolean
} // end of PaginationScrollListener
