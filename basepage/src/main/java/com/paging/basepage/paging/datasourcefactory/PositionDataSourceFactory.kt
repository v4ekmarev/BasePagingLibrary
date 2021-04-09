package com.paging.basepage.paging.datasourcefactory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.paging.basepage.paging.datasource.PageKeyDataSource
import com.paging.basepage.paging.datasource.PositionDataSource
import kotlinx.coroutines.CoroutineScope

/**
 * Data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI.
 *
 * @see DataSource.Factory
 */
class PositionDataSourceFactory<Value> constructor(
    private val scope: CoroutineScope,
    private val request: suspend () -> MutableList<Value>
) : DataSource.Factory<Int, Value>() {

    var sourceLiveData = MutableLiveData<PositionDataSource<Value>>()
    private var dataSource: PositionDataSource<Value>? = null

    /**
     * Create a DataSource.
     * @return The new DataSource.
     * @see DataSource.Factory.create
     */
    override fun create(): DataSource<Int, Value> {
        dataSource = PositionDataSource(request = request, scope = scope)
        sourceLiveData.postValue(dataSource)
        return dataSource as PositionDataSource<Value>
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
