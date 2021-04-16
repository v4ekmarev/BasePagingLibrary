package com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates.adapter.holders

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
    inflater: LayoutInflater
) : BaseViewHolder<ListItemLoadingBinding>(
    binding = ListItemLoadingBinding.inflate(inflater)
) {
    fun bind() {

    }
}
