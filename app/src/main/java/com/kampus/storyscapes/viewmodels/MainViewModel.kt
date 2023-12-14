package com.kampus.storyscapes.viewmodels

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.kampus.storyscapes.activity.ListStoryActivity
import com.kampus.storyscapes.api.ApiConfig
import com.kampus.storyscapes.model.LoginResponse
import com.kampus.storyscapes.model.Story
import com.kampus.storyscapes.model.TokenPreferences
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainViewModel(private  val tokenPreferences: TokenPreferences) : ViewModel() {


    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().loginUser(email, password)
        var token: String? = ""
        client.enqueue(object : Callback, retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val body: LoginResponse? = response.body()
                    token = body?.loginResult?.token
                    tokenPreferences.saveToken(token!!)
                    _loginResponse.value = response.body()
                } else {
                    Log.d("GagalLogin", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Log.d("ApaLogin", "onFailure: ${t.message}")
            }
        })
    }
}