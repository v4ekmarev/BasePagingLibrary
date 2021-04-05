package com.paging.basepaginglibrary.ui.main.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.paging.basepaginglibrary.ui.main.model.CharacterItem
import com.paging.basepaginglibrary.ui.main.model.CharacterItemMapper
import com.paging.basepaginglibrary.ui.main.paging.pagekeyed.CharactersPageKeyedDataSource
import com.paging.basepaginglibrary.ui.main.paging.pagekeyed.Param
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

    var sourceLiveData = MutableLiveData<CharactersPageKeyedDataSource>()
    private var keyedDataSource: CharactersPageKeyedDataSource? = null

    private var param: P? = null

    /**
     * Create a DataSource.
     *
     * @return The new DataSource.
     * @see DataSource.Factory.create
     */
    override fun create(): DataSource<Int, CharacterItem> {
        keyedDataSource = CharactersPageKeyedDataSource(repository, scope, mapper)
        param?.let { keyedDataSource?.param = it as Param }
        sourceLiveData.postValue(keyedDataSource)
        return keyedDataSource as CharactersPageKeyedDataSource
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
        keyedDataSource?.param = param as Param
    }
}
