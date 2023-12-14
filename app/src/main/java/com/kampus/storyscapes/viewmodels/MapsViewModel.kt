
package com.kampus.storyscapes.viewmodels

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kampus.storyscapes.api.ApiConfig
import com.kampus.storyscapes.model.StoriesResponse
import com.kampus.storyscapes.model.Story
import com.kampus.storyscapes.model.StoryResponse
import com.kampus.storyscapes.model.TokenPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel( private  val tokenPreferences: TokenPreferences) : ViewModel() {

    private val token : MutableLiveData<String> = MutableLiveData()

    private val stories : MutableLiveData<List<Story>> = MutableLiveData()

    fun getToken() : LiveData<String>
    {
        token.value = tokenPreferences.getToken()
        return token
    }

    fun getStoriesWithLocation(token : String)  : LiveData<List<Story>>
    {

        val client = ApiConfig.getApiService().getAllStoriesWithLocation("Bearer $token",1)

        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                val responseBody = response.body()
                if(responseBody != null)
                {
                    stories.postValue(responseBody.listStory)
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {

            }

        })

        return stories
    }



}