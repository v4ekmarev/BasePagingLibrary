package com.paging.basepaginglibrary.ui.main.paginwithdelegates

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paging.adapterdelegates.AdapterDelegatesManager
import com.paging.basepaginglibrary.R
import com.paging.basepaginglibrary.ui.main.adapterdelegates.*
import com.paging.basepaginglibrary.ui.main.adapterdelegates.model.DisplayableItem

class PaginationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagination)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val delegatesManager: AdapterDelegatesManager<List<DisplayableItem>> =
            AdapterDelegatesManager<List<DisplayableItem>>()
                .addDelegate(AdvertisementAdapterDelegate())
                .addDelegate(CatAdapterDelegate())
                .addDelegate(DogAdapterDelegate())
                .addDelegate(GeckoAdapterDelegate())
                .addDelegate(SnakeListItemAdapterDelegate())
//                .setFallbackDelegate(LoadingAdapterDelegate(this))

        val adapter: PagedListDelegationAdapter<DisplayableItem> =
            PagedListDelegationAdapter(
                itemsSame = { old, new -> if (old == new) true else old.javaClass == new.javaClass },
                contentsSame = { _, _ -> false },
                delegatesManager
            )

        recyclerView.adapter = adapter
        val pagedListLiveData: LiveData<PagedList<DisplayableItem>> =
            LivePagedListBuilder(SampleDataSource.Factory(), 20)
                .setBoundaryCallback(object : PagedList.BoundaryCallback<DisplayableItem?>() {
                    override fun onZeroItemsLoaded() {
                        Log.d("PaginationSource", "onZeroItemsLoaded")
                        super.onZeroItemsLoaded()
                    }

                    override fun onItemAtFrontLoaded(itemAtFront: DisplayableItem) {
                        Log.d("PaginationSource", "onItemAtFrontLoaded $itemAtFront")
                        super.onItemAtFrontLoaded(itemAtFront)
                    }

                    override fun onItemAtEndLoaded(itemAtEnd: DisplayableItem) {
                        Log.d("PaginationSource", "onItemAtEndLoaded $itemAtEnd")
                        super.onItemAtEndLoaded(itemAtEnd)
                    }
                })
                .build()
        pagedListLiveData.observe(
            this,
            { displayableItems -> adapter.submitList(displayableItems) })
    }
}