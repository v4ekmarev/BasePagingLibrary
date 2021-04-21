package com.paging.basepaginglibrary.ui.main.adapterdelegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paging.adapterdelegates.AbsFallbackAdapterDelegate
import com.paging.basepaginglibrary.R
import com.paging.basepaginglibrary.ui.main.adapterdelegates.model.DisplayableItem

class ReptilesFallbackDelegate :
    AbsFallbackAdapterDelegate<List<DisplayableItem>?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): RecyclerView.ViewHolder {
        val view: View = inflater.inflate(R.layout.item_unknown_reptile, parent, false)
        return ReptileFallbackViewHolder(view)
    }

    override fun onBindViewHolder(
        items: List<DisplayableItem>?,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any?>
    ) {
    }

    internal inner class ReptileFallbackViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}