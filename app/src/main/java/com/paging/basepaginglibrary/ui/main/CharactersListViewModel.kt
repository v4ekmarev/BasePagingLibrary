package com.paging.basepaginglibrary.ui.main

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import com.paging.basepaginglibrary.Injection
import com.paging.basepaginglibrary.ui.base.paging.ListViewState
import com.paging.basepaginglibrary.ui.base.paging.NetworkState
import com.paging.basepaginglibrary.ui.main.model.CharacterItemMapper
import com.paging.basepaginglibrary.ui.main.paging.CharactersPageDataSourceFactory
import com.paging.basepaginglibrary.ui.main.paging.PAGE_MAX_ELEMENTS
import com.paging.basepaginglibrary.ui.main.paging.Param
import com.paging.basepaginglibrary.ui.network.repositories.MarvelRepository
import kotlinx.coroutines.CoroutineScope

/**
 * View model responsible for preparing and managing the data for [CharactersListFragment].
 *
 * @see ViewModel
 */
class CharactersListViewModel : ViewModel() {


    protected val dataSourceFactory =
        CharactersPageDataSourceFactory<Param>(
            viewModelScope,
            CharacterItemMapper(),
            Injection.provideMarvelRepository()
        )

    private val networkState = Transformations.switchMap(dataSourceFactory.sourceLiveData) {
        it.networkState
    }

    val data = LivePagedListBuilder(dataSourceFactory, PAGE_MAX_ELEMENTS).build()
    val state = Transformations.map(networkState) {
        when (it) {
            is NetworkState.Success ->
                if (it.isAdditional && it.isEmptyResponse) {
                    ListViewState.NoMoreElements
                } else if (it.isEmptyResponse) {
                    ListViewState.Empty
                } else {
                    ListViewState.Loaded
                }
            is NetworkState.Loading ->
                if (it.isAdditional) {
                    ListViewState.AddLoading
                } else {
                    ListViewState.Loading
                }
            is NetworkState.Error ->
                if (it.isAdditional) {
                    ListViewState.AddError
                } else {
                    ListViewState.Error
                }
        }
    }

    /**
     * Refresh characters fetch them again and update the list.
     */
    fun refreshLoadedCharactersList() {
        dataSourceFactory.refresh()
    }

    /**
     * Retry last fetch operation to add characters into list.
     */
    fun retryAddCharactersList() {
        dataSourceFactory.retry()
    }
}
