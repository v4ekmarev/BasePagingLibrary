package com.paging.basepaginglibrary.ui.main.adapter.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paging.basepaginglibrary.databinding.ListItemLoadingBinding
import com.paging.basepaginglibrary.ui.base.BaseViewHolder

/**
 * Class describes characters loading view and metadata about its place within the [RecyclerView].
 *
 * @see BaseViewHolder
 */
class LoadingViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup
) : BaseViewHolder<ListItemLoadingBinding>(
    binding = ListItemLoadingBinding.inflate(inflater, parent, false)
) {
    fun bind() {

    }
}
