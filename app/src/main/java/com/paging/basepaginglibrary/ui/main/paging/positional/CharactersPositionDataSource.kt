package com.paging.basepaginglibrary.ui.main.paging.positional

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PositionalDataSource
import com.paging.basepage.paging.NetworkState
import com.paging.basepaginglibrary.ui.main.model.CharacterItem
import com.paging.basepaginglibrary.ui.main.model.CharacterItemMapper
import com.paging.basepaginglibrary.ui.main.paging.pagekeyed.PAGE_INIT_ELEMENTS
import com.paging.basepaginglibrary.ui.main.paging.pagekeyed.PAGE_MAX_ELEMENTS
import com.paging.basepaginglibrary.ui.main.paging.pagekeyed.Param
import com.paging.basepaginglibrary.ui.network.repositories.MarvelRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Этот DataSource позволяет запрашивать данные по позиции.
 * Т.е. если тянем данные, например, из БД, то можем указать, с какой позиции и сколько данных грузить.
 * Если данные из файла, то указываем с какой строки и сколько строк грузить.

 *   PagedList сам определяет необходимую позицию, с которой надо грузить очередную порцию данных,
 * и передает ее в DataSource.
 */
/**
 * Incremental data loader for page-keyed content, where requests return keys for next/previous
 * pages. Obtaining paginated the Marvel characters.
 *
 * @see PageKeyedDataSource
 */
class CharactersPositionDataSource constructor(
    private val repository: MarvelRepository,
    private val scope: CoroutineScope,
    private val mapper: CharacterItemMapper
) : PositionalDataSource<CharacterItem>() {

    val networkState = MutableLiveData<NetworkState>()

    var param = Param()

    var retry: (() -> Unit)? = null

    /**
     * Load initial data.
     *
     * @param params Parameters for initial load, including requested load size.
     * @param callback Callback that receives initial load data.
     * @see PageKeyedDataSource.loadInitial
     */
//    override fun loadInitial(
//        params: LoadInitialParams<Int>,
//        callback: LoadInitialCallback<Int, CharacterItem>
//    ) {
//        networkState.postValue(NetworkState.Loading())
//        scope.launch(
//            CoroutineExceptionHandler { _, _ ->
//                retry = {
//                    loadInitial(params, callback)
//                }
//                networkState.postValue(NetworkState.Error())
//            }
//        ) {
//            val response = repository.getCharacters(
//                offset = PAGE_INIT_ELEMENTS,
//                limit = PAGE_MAX_ELEMENTS
//            )
//            val data = mapper.map(response)
//            callback.onResult(data, null, PAGE_MAX_ELEMENTS)
//            networkState.postValue(NetworkState.Success(isEmptyResponse = data.isEmpty()))
//        }
//    }

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<CharacterItem>
    ) {
        networkState.postValue(NetworkState.Loading())
        scope.launch(
            CoroutineExceptionHandler { _, _ ->
                retry = {
                    loadInitial(params, callback)
                }
                networkState.postValue(NetworkState.Error())
            }
        ) {
            val response = repository.getCharacters(
                offset = PAGE_INIT_ELEMENTS,
                limit = PAGE_MAX_ELEMENTS
            )
            val data = mapper.map(response)
            callback.onResult(data, 0, PAGE_MAX_ELEMENTS)
            networkState.postValue(NetworkState.Success(isEmptyResponse = data.isEmpty()))
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CharacterItem>) {
        networkState.postValue(NetworkState.Loading(true))
        scope.launch(
            CoroutineExceptionHandler { _, _ ->
                retry = {
                    loadRange(params, callback)
                }
                networkState.postValue(NetworkState.Error(true))
            }
        ) {
            val response = repository.getCharacters(
                offset = params.loadSize,
                limit = PAGE_MAX_ELEMENTS
            )
            val data = mapper.map(response)
            callback.onResult(data)
            networkState.postValue(NetworkState.Success(true, data.isEmpty()))
        }
    }

    /**
     * Force retry last fetch operation in case it has ever been previously executed.
     */
    fun retry() {
        retry?.invoke()
    }
}

data class Param(val limit: Int = 50, val keyOffset: String)

