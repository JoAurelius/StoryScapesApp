package com.kampus.storyscapes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kampus.storyscapes.model.TokenPreferences
import java.lang.IllegalArgumentException

class ViewModelFactory(private val token : TokenPreferences) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        when {
            modelClass.isAssignableFrom(MapsViewModel::class.java)  ->
            {
                return MapsViewModel(token)  as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java)  ->
            {
                return MainViewModel(token)  as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java)  ->
            {
                return AddStoryViewModel(token)  as T
            }
        }

        throw IllegalArgumentException("No view model classes matched")
    }
}