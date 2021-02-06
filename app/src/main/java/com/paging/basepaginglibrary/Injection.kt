package com.paging.basepaginglibrary

import com.paging.basepaginglibrary.ui.network.MarvelService
import com.paging.basepaginglibrary.ui.network.repositories.MarvelRepository

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    fun provideMarvelRepository(): MarvelRepository {
        return MarvelRepository(MarvelService.create())
    }

}
