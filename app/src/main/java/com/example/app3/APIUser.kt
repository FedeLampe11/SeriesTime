package com.example.app3

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.0.113:8080/") //localhost
    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
    .build()

val userService = retrofit.create(APIUser::class.java)

interface APIUser {
    @POST("v1/users")
    suspend fun postNewUser(@Body body: Map<String, String?>): UserAuthReply

    @GET("v1/users")
    suspend fun loginUser(@Query ("email") email: String, @Query("password") password: String): UserAuthReply

    @GET("v1/favorites")
    suspend fun getFavourite(@Query ("user_id") userId: Long): List<Long>

}