package com.example.mypersonalproject.api

import retrofit2.Call
import retrofit2.http.GET
import com.example.mypersonalproject.models.Post

interface PostsApi {
    @GET("posts")
    fun getPosts(): Call<List<Post>>
}
