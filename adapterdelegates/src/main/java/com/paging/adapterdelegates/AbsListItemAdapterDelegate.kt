package com.paging.adapterdelegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * A simplified [AdapterDelegate] when the underlying adapter's dataset is a  [ ]. This class helps to reduce writing boilerplate code like casting list item and casting
 * ViewHolder.
 *
 *
 *
 * For instance if you have a list of animals (different kind of animals in classes like Cat, Dog
 * etc. assuming all have a common super class Animal) you want to display in your adapter and
 * you want to create a CatAdapterDelegate then this class would look like this:
 * `class CatAdapterDelegate extends AbsListItemAdapterDelegate<Cat, Animal, CatViewHolder>{
 *
 * <I>  The type of the item that is managed by this AdapterDelegate. Must be a subtype of T
 * <T>  The generic type of the list, in other words: { List<T>}
 * <VH> The type of the ViewHolder
 * Hannes Dorfmann
 * protected boolean isForViewType(Animal item, List<Animal> items, position){
 * return item instanceof Cat;
 * }
 * public CatViewHolder onCreateViewHolder(ViewGroup parent){
 * return new CatViewHolder(inflater.inflate(R.layout.item_cat, parent, false));
 * }
 * protected void onBindViewHolder(Cat item, CatViewHolder holder);
 * holder.setName(item.getName());
 * ...
 * }
` *
 *
 *
 * }
 *
 * @since 1.2
 */
abstract class AbsListItemAdapterDelegate<I : T, T, VH : RecyclerView.ViewHolder> :
    AdapterDelegate<List<T>>() {

    override fun isForViewType(items: List<T>, position: Int): Boolean {
        return isForViewType(items[position], items, position)
    }

    override fun onBindViewHolder(
        items: List<T>?, position: Int,
        holder: RecyclerView.ViewHolder, payloads: List<Any?>
    ) {
        onBindViewHolder(items?.get(position) as I, holder as VH, payloads)
    }

    /**
     * Called to determine whether this AdapterDelegate is the responsible for the given item in the
     * list or not
     * element
     *
     * @param item     The item from the list at the given position
     * @param items    The items from adapters dataset
     * @param position The items position in the dataset (list)
     * @return true if this AdapterDelegate is responsible for that, otherwise false
     */
    protected abstract fun isForViewType(item: T, items: List<T>, position: Int): Boolean

    /**
     * Creates the  [RecyclerView.ViewHolder] for the given data source item
     *
     * @param parent The ViewGroup parent of the given datasource
     * @return ViewHolder
     */
    abstract override fun onCreateViewHolder(parent: ViewGroup, inflater: LayoutInflater): VH

    /**
     * Called to bind the [RecyclerView.ViewHolder] to the item of the dataset
     *
     * @param item     The data item
     * @param holder   The ViewHolder
     * @param payloads The payloads
     */
    protected abstract fun onBindViewHolder(
        item: I, holder: VH,
        payloads: List<Any?>
    )
}