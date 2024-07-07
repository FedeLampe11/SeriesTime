package com.example.app3

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("https://www.episodate.com/api/")
    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
    .build()

val seriesService = retrofit.create(APIService::class.java)

interface APIService {
    @GET("most-popular?page=1")
    suspend fun getMostPopular(): Reply

    @GET("show-details")
    suspend fun getDetailPage(@Query("q") id: Long): EpReply

    @GET("search")
    suspend fun getSearchPage(@Query("q") query: String): Reply
}