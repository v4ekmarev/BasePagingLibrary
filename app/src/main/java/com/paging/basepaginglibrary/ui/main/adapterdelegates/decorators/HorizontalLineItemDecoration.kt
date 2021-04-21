package com.paging.basepaginglibrary.ui.main.adapterdelegates.decorators

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

class HorizontalLineItemDecoration(private val decorationProvider: ItemDecorationProvider) :
    RecyclerView.ItemDecoration() {

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        if (!decorationProvider.hasDivider())) return
        super.onDraw(c, parent, state)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }
}