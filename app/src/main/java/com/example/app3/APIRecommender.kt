package com.example.app3

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("https://vercel-seven-dun.vercel.app")
    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
    .build()

val recommenderService = retrofit.create(APIRecommender::class.java)

interface APIRecommender{
    @GET("/")
    suspend fun getRecommended(@Query ("id") userId: Long): List<Details>
}