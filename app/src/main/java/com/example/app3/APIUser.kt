package com.example.app3

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("http://localhost:8080/") //localhost
    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
    .build()

val userService = retrofit.create(APIUser::class.java)

interface APIUser {
    @POST("v1/users")
    suspend fun postNewUser(@Body body: Map<String, String?>): UserAuthReply

}