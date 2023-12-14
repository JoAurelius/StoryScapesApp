package com.kampus.storyscapes.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kampus.storyscapes.api.ApiService
import com.kampus.storyscapes.model.Story


class StoryPagingSource(
    private val apiService: ApiService,
    private val token : String
) : PagingSource<Int, Story>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                apiService.getStoriesWithPageAndSize("Bearer $token", position, params.loadSize).listStory
            Log.d("http", responseData.toString())
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.d("http", e.toString())
            return LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}