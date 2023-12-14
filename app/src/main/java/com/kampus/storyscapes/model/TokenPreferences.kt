package com.kampus.storyscapes.model

import android.content.Context
import android.content.SharedPreferences

class TokenPreferences (context: Context){

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("TOKEN_MANAGER", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("TOKEN", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("TOKEN").apply()
    }

}