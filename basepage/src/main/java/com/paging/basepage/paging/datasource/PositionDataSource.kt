//package com.paging.basepage.paging.datasource
//
//import androidx.lifecycle.MutableLiveData
//import androidx.paging.PageKeyedDataSource
//import androidx.paging.PositionalDataSource
//import com.paging.basepage.paging.states.NetworkState
//import com.paging.basepage.paging.PAGE_MAX_ELEMENTS
//import kotlinx.coroutines.CoroutineExceptionHandler
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//
//
///**
// * Этот DataSource позволяет запрашивать данные по позиции.
// * Т.е. если тянем данные, например, из БД, то можем указать, с какой позиции и сколько данных грузить.
// * Если данные из файла, то указываем с какой строки и сколько строк грузить.
//
// *   PagedList сам определяет необходимую позицию, с которой надо грузить очередную порцию данных,
// * и передает ее в DataSource.
// */
///**
// * Incremental data loader for page-keyed content, where requests return keys for next/previous
// * pages. Obtaining paginated the Marvel characters.
// *
// * @see PageKeyedDataSource
// */
//class PositionDataSource<Value> constructor(
//    private val request: suspend ()-> MutableList<Value>,
//    private val scope: CoroutineScope,
//) : PositionalDataSource<Value>() {
//
//    val networkState = MutableLiveData<NetworkState>()
//
//    var retry: (() -> Unit)? = null
//
//    override fun loadInitial(
//        params: LoadInitialParams,
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
//            val data = request.invoke()
//            callback.onResult(data, 0, PAGE_MAX_ELEMENTS)
//            networkState.postValue(NetworkState.Success(isEmptyResponse = data.isEmpty()))
//        }
//    }
//
//    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Value>) {
//        networkState.postValue(NetworkState.Loading(true))
//        scope.launch(
//            CoroutineExceptionHandler { _, _ ->
//                retry = {
//                    loadRange(params, callback)
//                }
//                networkState.postValue(NetworkState.Error(true))
//            }
//        ) {
//            val data = request.invoke()
//            callback.onResult(data)
//            networkState.postValue(NetworkState.Success(true, data.isEmpty()))
//        }
//    }
//
//    /**
//     * Force retry last fetch operation in case it has ever been previously executed.
//     */
//    fun retry() {
//        retry?.invoke()
//    }
//}
