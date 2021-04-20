/*
 * Copyright (c) 2015 Hannes Dorfmann.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.paging.basepaginglibrary.ui.main.adapterdelegates

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paging.basepage.adapterdelegates.AdapterDelegate
import com.paging.basepaginglibrary.R
import com.paging.basepaginglibrary.ui.main.adapterdelegates.model.Cat
import com.paging.basepaginglibrary.ui.main.adapterdelegates.model.DisplayableItem


class CatAdapterDelegate : AdapterDelegate<List<DisplayableItem>>() {


    override fun isForViewType(items: List<DisplayableItem>, position: Int): Boolean {
        return items[position] is Cat
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): RecyclerView.ViewHolder {
        Log.d("Scroll", "CatAdapterDelegate createViewHolder ")
        return CatViewHolder(inflater.inflate(R.layout.item_cat, parent, false))
    }

    override fun onBindViewHolder(
        items: List<DisplayableItem>?,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any?>
    ) {
        val vh = holder as CatViewHolder
        val cat: Cat = items?.get(position) as Cat
        vh.name.text = cat.name
        Log.d("Scroll", "CatAdapterDelegate bind  $position")
    }

    internal class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById<View>(R.id.name) as TextView
    }
}