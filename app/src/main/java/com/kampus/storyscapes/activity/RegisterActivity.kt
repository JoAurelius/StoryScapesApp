package com.kampus.storyscapes.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.kampus.storyscapes.R
import com.kampus.storyscapes.api.ApiConfig
import com.kampus.storyscapes.databinding.ActivityRegisterBinding
import com.kampus.storyscapes.model.GeneralResponse
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val passwordInputView = binding.edRegisterPassword
        passwordInputView.errorObserver.observe(this, Observer { hasError ->
            binding.btnRegister.isEnabled = !hasError
        })



        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

            if (name.isEmpty()) {
                binding.edRegisterName.setError("Nama tidak boleh kosong")
            } else if (email.isEmpty()) {
                binding.edRegisterEmail.setError("Email tidak boleh kosong")
            } else if (password.isEmpty()) {
                binding.btnRegister.isEnabled = false
            } else {
               registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().registerUser(name, email, password)
        client.enqueue(object : Callback, retrofit2.Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.isSuccessful) {
                    val registerResponse: GeneralResponse? = response.body()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)

                    Log.d("BerhasilRegister", "token: $registerResponse")
                } else {
                    Log.d("GagalRegister", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<GeneralResponse>, t: Throwable) {
                Log.d("ApaRegister", "onFailure: ${t.message}")
            }
        })
    }
}