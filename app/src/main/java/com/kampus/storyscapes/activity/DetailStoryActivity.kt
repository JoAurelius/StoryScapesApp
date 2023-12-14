package com.kampus.storyscapes.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.kampus.storyscapes.R
import com.kampus.storyscapes.api.ApiConfig
import com.kampus.storyscapes.databinding.ActivityDetailStoryBinding
import com.kampus.storyscapes.model.Story
import com.kampus.storyscapes.model.StoryResponse
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = intent.getStringExtra(TOKEN)
        val id = intent.getStringExtra(ID)
        Log.d("Token", token.toString())
        Log.d("ID", id.toString())
        getDetailStoryData(id!!, token!!)
    }

    private fun getDetailStoryData(id : String, token : String) {
        val client = ApiConfig.getApiService().getStory(id,"Bearer $token")
        client.enqueue(object : Callback, retrofit2.Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val story = response.body()?.story
                    if (story != null) {
                        binding.tvDetailName.text = story.name
                        binding.tvDetailDescription.text = story.description
                        Glide.with(this@DetailStoryActivity)
                            .load(story.photoUrl)
                            .into(binding.ivDetailPhoto)
                    }
                    Log.d("Story", story.toString())
                } else {
                    Log.d("Token Failure", response.message())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.d("Failure", t.message.toString())
            }
        })
    }

    companion object {
        val TOKEN: String? = "token"
        val ID: String? = "id"
    }
}