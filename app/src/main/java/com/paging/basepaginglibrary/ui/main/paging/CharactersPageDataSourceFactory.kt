package com.paging.basepaginglibrary.ui.main.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.paging.basepaginglibrary.ui.main.model.CharacterItem
import com.paging.basepaginglibrary.ui.main.model.CharacterItemMapper
import com.paging.basepaginglibrary.ui.network.repositories.MarvelRepository
import kotlinx.coroutines.CoroutineScope

/**
 * Data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI.
 *
 * @see DataSource.Factory
 */
class CharactersPageDataSourceFactory<P> constructor(
    private val scope: CoroutineScope,
    private val mapper: CharacterItemMapper,
    private val repository: MarvelRepository,
) : DataSource.Factory<Int, CharacterItem>() {

    var sourceLiveData = MutableLiveData<CharactersPageDataSource>()
    private var dataSource: CharactersPageDataSource? = null

    private var param: P? = null

    /**
     * Create a DataSource.
     *
     * @return The new DataSource.
     * @see DataSource.Factory.create
     */
    override fun create(): DataSource<Int, CharacterItem> {
        dataSource = CharactersPageDataSource(repository, scope, mapper)
        param?.let { dataSource?.param = it as Param }
        sourceLiveData.postValue(dataSource)
        return dataSource as CharactersPageDataSource
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
        sourceLiveData.value?.retry()
    }

    /**
     * Applies a parameter to load data
     */
    fun setParam(param: P) {
        this.param = param
        dataSource?.param = param as Param
    }
}
