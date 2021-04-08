package com.paging.basepage.paging.states

/**
 * Different states for [CharactersListFragment].
 *
 * @see BaseViewState
 */
sealed class ListViewState : BaseViewState {

    /**
     * Refreshing characters list.
     */
    object Refreshing : ListViewState()

    /**
     * Loaded characters list.
     */
    object Loaded : ListViewState()

    /**
     * Loading characters list.
     */
    object Loading : ListViewState()

    /**
     * Loading on add more elements into characters list.
     */
    object AddLoading : ListViewState()

    /**
     * Empty characters list.
     */
    object Empty : ListViewState()

    /**
     * Error on loading characters list.
     */
    object Error : ListViewState()

    /**
     * Error on add more elements into characters list.
     */
    object AddError : ListViewState()

    /**
     * No more elements for adding into characters list.
     */
    object NoMoreElements : ListViewState()

    // ============================================================================================
    //  Public helpers methods
    // ============================================================================================

    /**
     * Check if current view state is [Refreshing].
     *
     * @return True if is refreshing state, otherwise false.
     */
    fun isRefreshing() = this is Refreshing

    /**
     * Check if current view state is [Loaded].
     *
     * @return True if is loaded state, otherwise false.
     */
    fun isLoaded() = this is Loaded

    /**
     * Check if current view state is [Loading].
     *
     * @return True if is loading state, otherwise false.
     */
    fun isLoading() = this is Loading

    /**
     * Check if current view state is [AddLoading].
     *
     * @return True if is add loading state, otherwise false.
     */
    fun isAddLoading() = this is AddLoading

    /**
     * Check if current view state is [Empty].
     *
     * @return True if is empty state, otherwise false.
     */
    fun isEmpty() = this is Empty

    /**
     * Check if current view state is [Error].
     *
     * @return True if is error state, otherwise false.
     */
    fun isError() = this is Error

    /**
     * Check if current view state is [AddError].
     *
     * @return True if is add error state, otherwise false.
     */
    fun isAddError() = this is AddError

    /**
     * Check if current view state is [NoMoreElements].
     *
     * @return True if is no more elements state, otherwise false.
     */
    fun isNoMoreElements() = this is NoMoreElements
}
