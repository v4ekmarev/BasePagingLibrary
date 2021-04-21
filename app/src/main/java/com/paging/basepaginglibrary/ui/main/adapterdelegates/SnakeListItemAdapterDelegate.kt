package com.paging.basepaginglibrary.ui.main.adapterdelegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paging.adapterdelegates.AbsListItemAdapterDelegate
import com.paging.basepaginglibrary.R
import com.paging.basepaginglibrary.ui.main.adapterdelegates.model.DisplayableItem
import com.paging.basepaginglibrary.ui.main.adapterdelegates.model.Snake

/**
 * @author Hannes Dorfmann
 */
class SnakeListItemAdapterDelegate :
    AbsListItemAdapterDelegate<Snake, DisplayableItem, SnakeListItemAdapterDelegate.SnakeViewHolder>() {

    override fun isForViewType(
        item: DisplayableItem, items: List<DisplayableItem>,
        position: Int
    ): Boolean {
        return item is Snake
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): SnakeViewHolder {
        return SnakeViewHolder(
            inflater.inflate(R.layout.item_snake, parent, false)
        )
    }

    override fun onBindViewHolder(item: Snake, holder: SnakeViewHolder, payloads: List<Any?>) {
        holder.name.text = item.name
        holder.race.text = item.race
    }

    class SnakeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var race: TextView = itemView.findViewById(R.id.race)

    }
}