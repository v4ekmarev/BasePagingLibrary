package com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates

import androidx.lifecycle.ViewModel
import com.paging.basepage.paging.pager.PagerFlow
import com.paging.basepaginglibrary.Injection
import com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates.model.CharacterItem
import com.paging.basepaginglibrary.ui.main.paginationwithoutdelegates.model.CharacterItemMapper

/**
 * View model responsible for preparing and managing the data for [CharactersListFragment].
 *
 * @see ViewModel
 */
class CharactersListViewModel : ViewModel() {

    private var request: suspend (Int, Int) -> MutableList<CharacterItem> =
        { offset: Int, pageSize: Int -> createRequest(offset, pageSize) }

    val pagerFlow = PagerFlow(
        request = request
    ).flow

    private suspend inline fun createRequest(
        offset: Int,
        pageSize: Int
    ): MutableList<CharacterItem> {
        val repository = Injection.provideMarvelRepository()
        val response = repository.getCharacters(
            offset = offset,
            limit = pageSize
        )
        return CharacterItemMapper().map(response).toMutableList()
    }
}
