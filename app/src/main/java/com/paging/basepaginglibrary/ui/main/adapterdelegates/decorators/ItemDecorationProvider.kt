package com.paging.basepaginglibrary.ui.main.adapterdelegates.decorators

interface ItemDecorationProvider {
    fun hasDivider(position: Int) : Boolean
}