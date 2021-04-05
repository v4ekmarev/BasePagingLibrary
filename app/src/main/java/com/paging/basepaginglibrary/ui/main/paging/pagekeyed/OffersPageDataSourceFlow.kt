package com.paging.basepaginglibrary.ui.main.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.paging.basepage.paging.NetworkState
import com.paging.basepaginglibrary.ui.main.model.CharacterItem
import com.paging.basepaginglibrary.ui.main.model.CharacterItemMapper
import com.paging.basepaginglibrary.ui.main.paging.pagekeyed.PAGE_MAX_ELEMENTS
import com.paging.basepaginglibrary.ui.main.paging.pagekeyed.Param
import com.paging.basepaginglibrary.ui.network.repositories.MarvelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Incremental data loader for page-keyed content, where requests return keys for next/previous
 * pages. Obtaining paginated the Marvel characters.
 *
 * @see PageKeyedDataSource
 */
class OffersPageDataSource constructor(
    private val repository: MarvelRepository,
    private val scope: CoroutineScope,
    private val mapper: CharacterItemMapper
) : PageKeyedDataSource<Int, CharacterItem>(){

    val networkState = MutableLiveData<NetworkState>()

    var retry: (() -> Unit)? = null

    var param = Param()

    /**
     * Load initial data.
     *
     * @param params Parameters for initial load, including requested load size.
     * @param callback Callback that receives initial load data.
     * @see PageKeyedDataSource.loadInitial
     */
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CharacterItem>
    ) {
        scope.launch {
            repository.getCharactersFlow(offset = param.offset, limit = param.limit)
                .onStart {
                    networkState.value = NetworkState.Loading(false)
                }
                .catch {
                    retry = {
                        loadInitial(params, callback)
                    }
                    networkState.value = NetworkState.Error(false, it)
                }
                .map {
                    mapper.map(it)
                }
                .collect {
                    callback.onResult(it, null, PAGE_MAX_ELEMENTS)
                    networkState.value = NetworkState.Success(isEmptyResponse = it.isEmpty())
                }
        }
    }


    /**
     * Append page with the key specified by [LoadParams.key].
     *
     * @param params Parameters for the load, including the key for the new page, and requested
     * load size.
     * @param callback Callback that receives loaded data.
     * @see PageKeyedDataSource.loadAfter
     */
    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CharacterItem>
    ) {
        scope.launch {
            repository.getCharactersFlow(offset = param.offset, limit = param.limit)
                .onStart {
                    networkState.value = NetworkState.Loading(true)
                }
                .catch {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.value = NetworkState.Error(true, it)
                }
                .map {
                    mapper.map(it)
                }
                .collect {
                    callback.onResult(it, params.key + 1)
                    networkState.value = NetworkState.Success(
                        isAdditional = true,
                        isEmptyResponse = it.isEmpty()
                    )
                }
        }
    }

    /**
     * Prepend page with the key specified by [LoadParams.key]
     *
     * @param params Parameters for the load, including the key for the new page, and requested
     * load size.
     * @param callback Callback that receives loaded data.
     * @see PageKeyedDataSource.loadBefore
     */
    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, CharacterItem>
    ) {
        // Ignored, since we only ever append to our initial load
    }

    /**
     * Force retry last fetch operation in case it has ever been previously executed.
     */
    fun retry() {
        retry?.invoke()
    }
}
