package com.paging.basepaginglibrary.ui.main.adapter.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paging.basepaginglibrary.databinding.ListItemErrorBinding
import com.paging.basepaginglibrary.ui.base.BaseViewHolder
import com.paging.basepaginglibrary.ui.base.OnClickItem

/**
 * Class describes characters error view and metadata about its place within the [RecyclerView].
 *
 * @see BaseViewHolder
 */
class ErrorViewHolder(
    inflater: LayoutInflater
) : BaseViewHolder<ListItemErrorBinding>(
    binding = ListItemErrorBinding.inflate(inflater)
) {

    fun bind(clickRetryAdd: OnClickItem<Void?>? = null) = with(binding) {
        retryButton.setOnClickListener {
            clickRetryAdd?.click(null)
        }

    }
}
