package com.paging.basepaginglibrary.ui.main.paging.pagekeyed

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.paging.basepage.paging.NetworkState
import com.paging.basepaginglibrary.ui.main.model.CharacterItem
import com.paging.basepaginglibrary.ui.main.model.CharacterItemMapper
import com.paging.basepaginglibrary.ui.network.repositories.MarvelRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val PAGE_INIT_ELEMENTS = 0
const val PAGE_MAX_ELEMENTS = 50

/**
 * Этот DataSource подходит для общения со Storage, который вместе с очередной порцией данных передает
 * нам какой-то ключ для получения следующей порции данных.

Это может быть постраничная загрузка с параметром page. Мы просим данные, например, с page = 4.
Storage возвращает нам их и сообщает, что следующую порцию можно получить, передав ему page с значением 5.

Для числовых значений page это выглядит бессмысленным, потому что идет просто прибавление единицы.
Но ключ может быть и текстовым. Например, так раньше работал API Youtube (не знаю, как сейчас).
Т.е. мы ищем видео по какому-то поисковому запросу. Youtube возвращает нам первую порцию данных и
с ними текстовый токен. В следующем запросе к Youtube мы передаем этот токен, чтобы получить следующую порцию результатов нашего поиска. И так далее.

Также сервер может в качестве ключа вообще передавать готовую ссылку, которую надо будет использовать,
чтобы получить следующую порцию данных.

В общем, основной смысл в том, что следующий запрос данных мы сможем сделать, использовав для этого некий ключ,
полученный в предыдущем запросе.
 */
/**
 * Incremental data loader for page-keyed content, where requests return keys for next/previous
 * pages. Obtaining paginated the Marvel characters.
 *
 * @see PageKeyedDataSource
 */
class CharactersPageKeyedDataSource constructor(
    private val repository: MarvelRepository,
    private val scope: CoroutineScope,
    private val mapper: CharacterItemMapper
) : PageKeyedDataSource<Int, CharacterItem>() {

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
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, CharacterItem>
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
            callback.onResult(data, null, PAGE_MAX_ELEMENTS)
            networkState.postValue(NetworkState.Success(isEmptyResponse = data.isEmpty()))
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
        networkState.postValue(NetworkState.Loading(true))
        scope.launch(
            CoroutineExceptionHandler { _, _ ->
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.Error(true))
            }
        ) {
            val response = repository.getCharacters(
                offset = params.key,
                limit = PAGE_MAX_ELEMENTS
            )
            val data = mapper.map(response)
            callback.onResult(data, params.key + PAGE_MAX_ELEMENTS)
            networkState.postValue(NetworkState.Success(true, data.isEmpty()))
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

data class Param(val limit: Int = 50, val offset: Int = 0)

