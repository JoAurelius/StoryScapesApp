package com.kampus.storyscapes.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kampus.storyscapes.R
import com.kampus.storyscapes.adapter.StoryAdapter
import com.kampus.storyscapes.adapter.StoryListAdapter
import com.kampus.storyscapes.api.ApiConfig
import com.kampus.storyscapes.databinding.ActivityListStoryBinding
import com.kampus.storyscapes.model.StoriesResponse
import com.kampus.storyscapes.model.Story
import com.kampus.storyscapes.model.TokenPreferences
import com.kampus.storyscapes.paging.StoryPagingSource
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var tokenManager: TokenPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenPreferences(this)
        val token = tokenManager.getToken().toString()
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        playAnimation()

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        getStoriesData(token)

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                tokenManager.clearToken()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            R.id.action_map -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getPagingAllStories(token : String) : LiveData<PagingData<Story>>
    {
         return Pager(config=PagingConfig(pageSize = 5, enablePlaceholders = true)) {
             StoryPagingSource(ApiConfig.getApiService(), token)
         }.liveData
    }
    private fun getStoriesData(token : String) {
        getPagingAllStories(token).observe(this) {

                val adapter = StoryListAdapter(token)
           adapter.submitData(lifecycle,it)
                binding.rvStory.adapter =adapter
                }
    }
    @SuppressLint("Recycle", "RestrictedApi")
    private fun playAnimation() {
        val animator = ObjectAnimator.ofFloat(binding.rvStory, View.TRANSLATION_X, -1000f, 0f)
        animator.duration = 1000
        animator.start()
    }

    companion object {
        const val EXTRA_TOKEN = "token"
    }



}