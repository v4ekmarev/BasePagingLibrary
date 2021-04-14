//package com.paging.basepage.paging.datasource
//
//import androidx.lifecycle.MutableLiveData
//import androidx.paging.ItemKeyedDataSource
//import androidx.paging.PageKeyedDataSource
//import com.paging.basepage.paging.PAGE_MAX_ELEMENTS
//import com.paging.basepage.paging.states.NetworkState
//import kotlinx.coroutines.CoroutineExceptionHandler
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//
///**
// * Этот DataSource подходит для общения со Storage, который вместе с очередной порцией данных передает
// * нам какой-то ключ для получения следующей порции данных.
//
//Это может быть постраничная загрузка с параметром page. Мы просим данные, например, с page = 4.
//Storage возвращает нам их и сообщает, что следующую порцию можно получить, передав ему page с значением 5.
//
//Для числовых значений page это выглядит бессмысленным, потому что идет просто прибавление единицы.
//Но ключ может быть и текстовым. Например, так раньше работал API Youtube (не знаю, как сейчас).
//Т.е. мы ищем видео по какому-то поисковому запросу. Youtube возвращает нам первую порцию данных и
//с ними текстовый токен. В следующем запросе к Youtube мы передаем этот токен, чтобы получить следующую порцию результатов нашего поиска. И так далее.
//
//Также сервер может в качестве ключа вообще передавать готовую ссылку, которую надо будет использовать,
//чтобы получить следующую порцию данных.
//
//В общем, основной смысл в том, что следующий запрос данных мы сможем сделать, использовав для этого некий ключ,
//полученный в предыдущем запросе.
// */
///**
// * Incremental data loader for page-keyed content, where requests return keys for next/previous
// * pages. Obtaining paginated the Marvel characters.
// *
// * @see PageKeyedDataSource
// */
//class ItemKeyedDataSource<Key, Value> constructor(
//    private val request: suspend () -> MutableList<Value>,
//    private val scope: CoroutineScope,
//) : ItemKeyedDataSource<Key, Value>() {
//
//    val networkState = MutableLiveData<NetworkState>()
//
//    var retry: (() -> Unit)? = null
//
//    /**
//     * Force retry last fetch operation in case it has ever been previously executed.
//     */
//    fun retry() {
//        retry?.invoke()
//    }
//
//    override fun getKey(item: Value): Key = (item as Params).getKey()
//
//
//    override fun loadInitial(
//        params: LoadInitialParams<Key>,
//        callback: LoadInitialCallback<Value>
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
//
//            val data = request.invoke()
//            callback.onResult(data, 0, PAGE_MAX_ELEMENTS)
//            networkState.postValue(NetworkState.Success(isEmptyResponse = data.isEmpty()))
//        }
//    }
//
//    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Value>) {
//        networkState.postValue(NetworkState.Loading(true))
//        scope.launch(
//            CoroutineExceptionHandler { _, _ ->
//                retry = {
//                    loadAfter(params, callback)
//                }
//                networkState.postValue(NetworkState.Error(true))
//            }
//        ) {
////            val response = repository.getCharacters(
////                offset = params.key,
////                limit = PAGE_MAX_ELEMENTS
////            )
////            val data = mapper.map(response)
//            val data = request.invoke()
//            callback.onResult(data)
//            networkState.postValue(NetworkState.Success(true, data.isEmpty()))
//        }
//    }
//
//    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Value>) {
//
//    }
//
//}
