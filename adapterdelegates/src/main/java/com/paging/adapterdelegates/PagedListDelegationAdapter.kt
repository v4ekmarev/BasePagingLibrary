package com.paging.adapterdelegates

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * A [PagedListAdapter] that uses [AdapterDelegatesManager]
 * and [com.hannesdorfmann.adapterdelegates4.AdapterDelegate]
 *
 * @param <T> The type of [PagedList]
</T> */
class PagedListDelegationAdapter<T>(
    itemsSame: (T, T) -> Boolean = { _, _ -> false },
    contentsSame: (T, T) -> Boolean = { _, _ -> false },
    protected var delegatesManager: AdapterDelegatesManager<List<T>>,
) : PagedListAdapter<T, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(old: T, new: T): Boolean = itemsSame(old, new)
    override fun areContentsTheSame(old: T, new: T): Boolean = contentsSame(old, new)
}) {


    /**
     * @param diffCallback The Callback
     * @param delegates    The [AdapterDelegate]s that should be added
     * @since 4.1.0
     */
    constructor(
        vararg delegates: AdapterDelegate<List<T>>
    ) : this(AdapterDelegatesManager<List<T>>()) {
        for (element in delegates) {
            delegatesManager.addDelegate(element)
        }
    }

    constructor(
        delegatesManager: AdapterDelegatesManager<List<T>>
    ) : this(
        { _, _ -> false },
        { _, _ -> false },
        delegatesManager = delegatesManager
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position) // Internally triggers loading items around items around the given position
        delegatesManager.onBindViewHolder(currentList, position, holder, null)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int,
        payloads: List<*>
    ) {
        getItem(position) // Internally triggers loading items around items around the given position
        delegatesManager.onBindViewHolder(currentList, position, holder, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(currentList, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return delegatesManager.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder)
    }
}