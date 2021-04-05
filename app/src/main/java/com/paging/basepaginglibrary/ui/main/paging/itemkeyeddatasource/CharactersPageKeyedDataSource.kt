package com.paging.basepaginglibrary.ui.main.paging.itemkeyeddatasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
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
) : ItemKeyedDataSource<Long, CharacterItem>() {

    val networkState = MutableLiveData<NetworkState>()

    var param = Param()

    var retry: (() -> Unit)? = null

    /**
     * Force retry last fetch operation in case it has ever been previously executed.
     */
    fun retry() {
        retry?.invoke()
    }

    override fun getKey(item: CharacterItem): Long = item.id


    override fun loadInitial(
        params: LoadInitialParams<Long>,
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

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<CharacterItem>) {
        networkState.postValue(NetworkState.Loading(true))
        scope.launch(
            CoroutineExceptionHandler { _, _ ->
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.Error(true))
            }
        ) {
//            val response = repository.getCharacters(
//                offset = params.key,
//                limit = PAGE_MAX_ELEMENTS
//            )
//            val data = mapper.map(response)
            val data = mutableListOf<CharacterItem>()
            callback.onResult(data)
            networkState.postValue(NetworkState.Success(true, data.isEmpty()))
        }
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<CharacterItem>) {

    }

}

data class Param(val limit: Int = 50, val keyOffset: String= "")

