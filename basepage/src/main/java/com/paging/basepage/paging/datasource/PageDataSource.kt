package com.paging.basepage.paging.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PageDataSource<Value : Any>(
    private val request: suspend (page: Int, pageSize: Int) -> MutableList<Value>
) : PagingSource<Int, Value>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val offset = params.key ?: 0

        return try {
            val data = request.invoke(offset, params.loadSize)
            LoadResult.Page(
                data = data,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = if (data.isEmpty()) null else offset + params.loadSize
            )
        } catch (exception: Exception) {
            LoadResult.Error(throwable = exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return null
    }

}