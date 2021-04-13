package com.paging.basepaginglibrary.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.paging.basepage.paging.datasourcefactory.PageKeyDataSourceFactory
import com.paging.basepage.paging.states.ListViewState
import com.paging.basepaginglibrary.Injection
import com.paging.basepaginglibrary.ui.main.model.CharacterItem
import com.paging.basepaginglibrary.ui.main.model.CharacterItemMapper

/**
 * View model responsible for preparing and managing the data for [CharactersListFragment].
 *
 * @see ViewModel
 */
class CharactersListViewModel : ViewModel() {

    companion object {
        private const val PAGE_MAX_ELEMENTS_CHARACTERS = 30
    }

    var request: suspend (Int) -> MutableList<CharacterItem> =
        { offset: Int -> createRequest(offset) }

    private val dataSourceFactory =
        PageKeyDataSourceFactory(
            scope = viewModelScope,
            request = request
        )

    fun state(): LiveData<ListViewState> {
        return dataSourceFactory.getListState()
    }

    fun getData(): LiveData<PagedList<CharacterItem>> {
        return dataSourceFactory.data
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

    private suspend inline fun createRequest(offset: Int): MutableList<CharacterItem> {
        val repository = Injection.provideMarvelRepository()
        val response = repository.getCharacters(
            offset = offset,
            limit = PAGE_MAX_ELEMENTS_CHARACTERS
        )
        return CharacterItemMapper().map(response).toMutableList()
    }
}
