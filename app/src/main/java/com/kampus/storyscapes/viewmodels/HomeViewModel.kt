package com.kampus.storyscapes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.kampus.storyscapes.model.Story
import com.kampus.storyscapes.model.StoryRepository

class HomeViewModel(private val repo: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<Story>> =
        repo.getStoryData()
}