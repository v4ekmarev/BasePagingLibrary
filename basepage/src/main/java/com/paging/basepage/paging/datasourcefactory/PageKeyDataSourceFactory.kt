package com.paging.basepage.paging.datasourcefactory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import com.paging.basepage.paging.PAGE_MAX_ELEMENTS
import com.paging.basepage.paging.TransformState
import com.paging.basepage.paging.datasource.PageKeyDataSource
import com.paging.basepage.paging.states.ListViewState
import com.paging.basepage.paging.states.NetworkState
import kotlinx.coroutines.CoroutineScope

/**
 * Data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI.
 *
 * @see DataSource.Factory
 */
class PageKeyDataSourceFactory<Value>(
    private val scope: CoroutineScope,
    private var request: suspend (offset: Int) -> MutableList<Value>
) : DataSource.Factory<Int, Value>() {

    var sourceLiveData = MutableLiveData<PageKeyDataSource<Value>>()
    private var dataSource: PageKeyDataSource<Value>? = null


    fun getListState(): LiveData<ListViewState> {
        val networkState = Transformations.switchMap(sourceLiveData) {
            it.networkState
        }
        return TransformState.transformState(networkState)
    }

    fun getNetworkState(): LiveData<NetworkState>? {
        return Transformations.switchMap(sourceLiveData) {
            it.networkState
        }
    }

    fun setRequest(request: suspend (offset: Int) -> MutableList<Value>) {
        this.request = request
    }

    val data = LivePagedListBuilder(this, PAGE_MAX_ELEMENTS).build()

    /**
     * Create a DataSource.
     * @return The new DataSource.
     * @see DataSource.Factory.create
     */
    override fun create(): DataSource<Int, Value> {
        dataSource = PageKeyDataSource(request = request, scope)
        sourceLiveData.postValue(dataSource)
        return dataSource as PageKeyDataSource<Value>
    }

    /**
     * Force refresh data source by invalidating and re-create again.
     */
    fun refresh() {
        sourceLiveData.value?.invalidate()
    }

    /**
     * Force retry last fetch operation on data source.
     */
    fun retry() {
        sourceLiveData.value?.retry?.let { it() }
    }
}
