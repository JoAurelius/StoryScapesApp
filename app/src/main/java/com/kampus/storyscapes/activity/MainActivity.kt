package com.kampus.storyscapes.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kampus.storyscapes.api.ApiConfig
import com.kampus.storyscapes.databinding.ActivityMainBinding
import com.kampus.storyscapes.model.LoginResponse
import com.kampus.storyscapes.model.TokenPreferences
import com.kampus.storyscapes.viewmodels.MainViewModel
import com.kampus.storyscapes.viewmodels.ViewModelFactory
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var factory : ViewModelProvider.Factory

    private lateinit var binding: ActivityMainBinding

    private lateinit var tokenPreferences : TokenPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenPreferences =  TokenPreferences(this)
        if (tokenPreferences.getToken() != null) {
            val intent = Intent(this, ListStoryActivity::class.java)
            intent.putExtra(ListStoryActivity.EXTRA_TOKEN, tokenPreferences.getToken())
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        setupViewModel()

        val tokenManager = TokenPreferences(this)


        val passwordInputView = binding.edLoginPassword
        passwordInputView.errorObserver.observe(this, Observer { hasError ->
            binding.btnLogin.isEnabled = !hasError
        })

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            mainViewModel.login(email, password)
            mainViewModel.loginResponse.observe(this, Observer { loginResponse ->
                val token = loginResponse.loginResult.token
                tokenPreferences.saveToken(token)
                val intent = Intent(this, ListStoryActivity::class.java)
                intent.putExtra(ListStoryActivity.EXTRA_TOKEN, token)
                startActivity(intent)
            })
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory(token = tokenPreferences)
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }


}