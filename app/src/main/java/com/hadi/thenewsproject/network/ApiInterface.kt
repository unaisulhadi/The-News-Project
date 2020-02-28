package com.hadi.thenewsproject.network

import com.hadi.thenewsproject.model.Headlines
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("top-headlines")
    fun getHeadlines(
        @Query("country") country:String,
        @Query("apiKey") apiKey:String
    ): Call<Headlines>

    @GET("everything")
    fun searchNews(
        @Query("q") query:String,
        @Query("apiKey") apiKey: String
    ) : Call<Headlines>


}