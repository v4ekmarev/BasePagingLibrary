//package com.paging.basepaginglibrary.ui.main.paging
//
//import android.util.Log
//import androidx.lifecycle.MutableLiveData
//import androidx.paging.PageKeyedDataSource
//import com.justcashback.domain.model.offers.Offer
//import com.justcashback.domain.usecases.offers.GetOffersUseCase
//import com.justcashback.ims_justcash.ui.view.NetworkState
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.launch
//import javax.inject.Provider
//
//const val PAGE_MAX_ELEMENTS = 10
//
///**
// * Incremental data loader for page-keyed content, where requests return keys for next/previous
// * pages. Obtaining paginated the Marvel characters.
// *
// * @see PageKeyedDataSource
// */
//class OffersPageDataSource constructor(
//    val getOffersUseCase: GetOffersUseCase,
//    val viewModelScope: CoroutineScope
//) : PageKeyedDataSource<Int, Offer>(){
//
//    val networkState = MutableLiveData<NetworkState>()
//
//    var param: GetOffersUseCase.GetOffersParam = GetOffersUseCase.GetOffersParam()
//
//    var retry: (() -> Unit)? = null
//
//    /**
//     * Load initial data.
//     *
//     * @param params Parameters for initial load, including requested load size.
//     * @param callback Callback that receives initial load data.
//     * @see PageKeyedDataSource.loadInitial
//     */
//    override fun loadInitial(
//        params: LoadInitialParams<Int>,
//        callback: LoadInitialCallback<Int, Offer>
//    ) {
//        viewModelScope.launch {
//            getOffersUseCase.execute(param)
//                .onStart {
//                    networkState.value = NetworkState.Loading(false)
//                }
//                .catch {
//                    retry = {
//                        loadInitial(params, callback)
//                    }
//                    networkState.value = NetworkState.Error(false, it)
//                }
//                .collect {
//                    callback.onResult(it.items, null, it.page + 1)
//                    networkState.value = NetworkState.Success(isEmptyResponse = it.items.isEmpty())
//                }
//        }
//    }
//
//
//    /**
//     * Append page with the key specified by [LoadParams.key].
//     *
//     * @param params Parameters for the load, including the key for the new page, and requested
//     * load size.
//     * @param callback Callback that receives loaded data.
//     * @see PageKeyedDataSource.loadAfter
//     */
//    override fun loadAfter(
//        params: LoadParams<Int>,
//        callback: LoadCallback<Int, Offer>
//    ) {
//        viewModelScope.launch {
//            param.pageNumber = params.key
//            getOffersUseCase.execute(param)
//                .onStart {
//                    networkState.value = NetworkState.Loading(true)
//                }
//                .catch {
//                    retry = {
//                        loadAfter(params, callback)
//                    }
//                    networkState.value = NetworkState.Error(true, it)
//                }
//                .collect {
//                    callback.onResult(it.items, params.key + 1)
//                    networkState.value = NetworkState.Success(
//                        isAdditional = true,
//                        isEmptyResponse = it.items.isEmpty()
//                    )
//                }
//        }
//    }
//
//    /**
//     * Prepend page with the key specified by [LoadParams.key]
//     *
//     * @param params Parameters for the load, including the key for the new page, and requested
//     * load size.
//     * @param callback Callback that receives loaded data.
//     * @see PageKeyedDataSource.loadBefore
//     */
//    override fun loadBefore(
//        params: LoadParams<Int>,
//        callback: LoadCallback<Int, Offer>
//    ) {
//        // Ignored, since we only ever append to our initial load
//    }
//
//    /**
//     * Force retry last fetch operation in case it has ever been previously executed.
//     */
//    fun retry() {
//        retry?.invoke()
//    }
//}
