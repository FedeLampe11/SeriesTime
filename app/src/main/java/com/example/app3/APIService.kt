package com.example.app3

import retrofit2.Retrofit
import retrofit2.http.GET

// "https://run.mocky.io"
private val retrofit = Retrofit.Builder().baseUrl("https://www.episodate.com/api/")
    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
    .build()

val seriesService = retrofit.create(APIService::class.java)

interface APIService {
    // "/v3/0d42d65e-77e7-4cd1-8183-0402011c1bf1"
    @GET("most-popular?page=1")
    suspend fun getCategories(): Reply
}