package com.example.app3

import retrofit2.Retrofit
import retrofit2.http.GET

private val retrofit = Retrofit.Builder().baseUrl("https://run.mocky.io")
    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
    .build()

val recipeService = retrofit.create(APIService::class.java)

interface APIService {
    @GET("/v3/0d42d65e-77e7-4cd1-8183-0402011c1bf1")
    suspend fun getCategories(): CategoriesResponse
}