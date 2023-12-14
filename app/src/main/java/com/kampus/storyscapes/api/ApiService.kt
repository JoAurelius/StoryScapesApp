package com.kampus.storyscapes.api

import com.kampus.storyscapes.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    // token eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVM1aENQenB6M0U0aThmZ0YiLCJpYXQiOjE2ODMwOTIwOTN9.DZ3o28NoAdqfRWJFvy_MuPinRPhUFxSoyUWTr4e2NEQ

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<GeneralResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Header("Authorization") token: String,
    ): Call<GeneralResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String
    ): Call<StoriesResponse>

    @GET("stories/{id}")
    fun getStory(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @Multipart
    @POST("/stories")
    fun uploadStory(
        @Header("Authorization") authToken: String,
        @Part("description") description: String,
        @Part photoFile: MultipartBody.Part,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): Call<GeneralResponse>

    @GET("stories")
    fun getAllStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") locationOnly : Int
    ) : Call<StoriesResponse>

    @GET("stories")
    suspend fun getStoriesWithPageAndSize(
        @Header("Authorization") token : String,
        @Query("page") page : Int,
        @Query("size") size : Int,
    ): StoriesResponse
}