package com.paging.basepage.adapterdelegates

import android.view.ViewGroup
import androidx.recyclerview.widget.*

/**
 * An implementation of an Adapter that already uses a [AdapterDelegatesManager] pretty same as
 * [AbsDelegationAdapter] but also uses [AsyncListDiffer] from support library 27.0.1 for
 * calculating diffs between old and new collections of items and does this on background thread.
 * That means that now you should not carry about [RecyclerView.Adapter.notifyItemChanged]
 * and other methods of adapter, all you need to do is to submit a new list into adapter and all diffs will be
 * calculated for you.
 * You just have to add the [AdapterDelegate]s i.e. in the constructor of a subclass that inheritance from this
 * class:
 * <pre>
 * `class MyAdapter extends AsyncListDifferDelegationAdapter<MyDataSourceType> {
 * public MyAdapter() {
 * this.delegatesManager.add(new FooAdapterDelegate())
 * .add(new BarAdapterDelegate());
 * }
 * }
` *
</pre> *
 *
 * @param <T> The type of the datasource / items. Internally we will use List&lt;T&gt; but you only have
 * to provide T (and not List&lt;T&gt;). Its safe to use this with
 * [AbsListItemAdapterDelegate].
 * @author Sergey Opivalov
 * @author Hannes Dorfmann
</T> */
class AsyncListDifferDelegationAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected val delegatesManager: AdapterDelegatesManager<List<T>>
    protected val differ: AsyncListDiffer<T>

    @JvmOverloads
    constructor(
        diffCallback: DiffUtil.ItemCallback<T>,
        delegatesManager: AdapterDelegatesManager<List<T>> = AdapterDelegatesManager()
    ) {
        if (diffCallback == null) {
            throw NullPointerException("ItemCallback is null")
        }
        if (delegatesManager == null) {
            throw NullPointerException("AdapterDelegatesManager is null")
        }
        differ = AsyncListDiffer(this, diffCallback)
        this.delegatesManager = delegatesManager
    }

    constructor(
        differConfig: AsyncDifferConfig<T>,
        delegatesManager: AdapterDelegatesManager<List<T>>
    ) {
        if (differConfig == null) {
            throw NullPointerException("AsyncDifferConfig is null")
        }
        if (delegatesManager == null) {
            throw NullPointerException("AdapterDelegatesManager is null")
        }
        differ = AsyncListDiffer<T>(AdapterListUpdateCallback(this), differConfig)
        this.delegatesManager = delegatesManager
    }

    /**
     * Adds a list of [AdapterDelegate]s
     *
     * @param delegates
     * @since 4.2.0
     */
    constructor(
        diffCallback: DiffUtil.ItemCallback<T>,
        vararg delegates: AdapterDelegate<List<T>>
    ) {
        if (diffCallback == null) {
            throw NullPointerException("ItemCallback is null")
        }
        differ = AsyncListDiffer(this, diffCallback)
        delegatesManager = AdapterDelegatesManager(*delegates)
    }

    /**
     * Adds a list of [AdapterDelegate]s
     *
     * @param delegates
     * @since 4.2.0
     */
    constructor(
        differConfig: AsyncDifferConfig<T>,
        vararg delegates: AdapterDelegate<List<T>>
    ) {
        if (differConfig == null) {
            throw NullPointerException("AsyncDifferConfig is null")
        }
        differ = AsyncListDiffer<T>(AdapterListUpdateCallback(this), differConfig)
        delegatesManager = AdapterDelegatesManager(*delegates)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(differ.currentList, position, holder, null)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<*>
    ) {
        delegatesManager.onBindViewHolder(differ.currentList, position, holder, payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.getItemViewType(differ.currentList, position)
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
    /**
     * Get the items / data source of this adapter
     *
     * @return The items / data source
     */
    /**
     * Set the items / data source of this adapter
     *
     * @param items The items / data source
     */
    var items: List<T>?
        get() = differ.currentList
        set(items) {
            differ.submitList(items)
        }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}