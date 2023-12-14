package com.kampus.storyscapes.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kampus.storyscapes.activity.ListStoryActivity
import com.kampus.storyscapes.api.ApiConfig
import com.kampus.storyscapes.model.GeneralResponse
import com.kampus.storyscapes.model.TokenPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class AddStoryViewModel(private  val tokenPreferences: TokenPreferences) : ViewModel() {

    private val _addStoryResponse = MutableLiveData<GeneralResponse>()
    val addStoryResponse: LiveData<GeneralResponse> = _addStoryResponse

    fun uploadStory(description: RequestBody, imageMultipart: MultipartBody.Part, token: String){

            val client =
                ApiConfig.getApiService().addNewStory(description, imageMultipart, "Bearer $token")

            client.enqueue(object : Callback, retrofit2.Callback<GeneralResponse> {
                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            _addStoryResponse.value = response.body()
                            Log.d("AddStoryActivityDone", "onResponse: ${responseBody.message}")
                        }
                    } else {
                        Log.d("AddStoryActivityFailed", "onResponse: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    Log.d("AddStoryActivityFailed", "onFailure: ${t.message.toString()}")
                }
            })
        }
}