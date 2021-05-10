package com.paging.basepage.paging.pager

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.paging.basepage.paging.datasource.PageDataSource

class PagerFlow<Value : Any>(
    pageSize: Int = 30,
    private val request: suspend (page: Int, pageSize: Int) -> MutableList<Value>
) {

    val flow = Pager(config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        pagingSourceFactory = { PageDataSource(request) }).flow

}