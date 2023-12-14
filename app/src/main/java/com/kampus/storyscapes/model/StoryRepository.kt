package com.kampus.storyscapes.model

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.kampus.storyscapes.api.ApiService
import com.kampus.storyscapes.paging.StoryPagingSource

class StoryRepository(private val apiService: ApiService, private val pref: TokenPreferences) {
    fun getStoryData(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, pref.getToken()!!)
            }
        ).liveData
    }
}