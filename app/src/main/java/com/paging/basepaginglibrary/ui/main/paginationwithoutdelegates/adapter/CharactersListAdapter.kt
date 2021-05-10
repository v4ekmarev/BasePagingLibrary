package com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paging.basepage.paging.BasePagedListAdapter
import com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates.adapter.holders.CharacterViewHolder
import com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates.model.CharacterItem

/**
 * Class for presenting characters List data in a [RecyclerView], including computing
 * diffs between Lists on a background thread.
 *
 * @see BaseListAdapter
 */
class CharactersListAdapter : BasePagedListAdapter<CharacterItem>(
    itemsSame = { old, new -> old.id == new.id },
    contentsSame = { old, new -> old == new }
) {

    /**
     * Called when RecyclerView needs a new [RecyclerView.ViewHolder] of the given type to
     * represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param inflater Instantiates a layout XML file into its corresponding View objects.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see BaseListAdapter.onCreateViewHolder
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder = CharacterViewHolder(inflater)

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @see BaseListAdapter.onBindViewHolder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as CharacterViewHolder).bind(it)
        }
    }
}

