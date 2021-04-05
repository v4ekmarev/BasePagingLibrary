package com.paging.basepaginglibrary.ui.main.adapter.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.paging.basepaginglibrary.databinding.ListItemCharacterBinding
import com.paging.basepaginglibrary.ui.base.BaseViewHolder
import com.paging.basepaginglibrary.ui.main.model.CharacterItem

/**
 * Class describes character view and metadata about its place within the [RecyclerView].
 *
 * @see BaseViewHolder
 */
class CharacterViewHolder(
    inflater: LayoutInflater
) : BaseViewHolder<ListItemCharacterBinding>(
    binding = ListItemCharacterBinding.inflate(inflater)
) {

    /**
     * Bind view data binding variables.
     *
     * @param viewModel Character list view model.
     * @param item Character list item.
     */
    fun bind(characterItem: CharacterItem) = with(binding) {
        characterName.text = characterItem.name

        Glide.with(characterImage.context)
            .load(characterItem.imageUrl)
            .into(characterImage)

    }
}
