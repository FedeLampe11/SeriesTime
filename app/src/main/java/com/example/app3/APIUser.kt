package com.example.app3

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("http://192.168.1.34:8080/") //localhost
//private val retrofit = Retrofit.Builder().baseUrl("http://spring-api-env.eba-grqwbz5b.eu-north-1.elasticbeanstalk.com/")
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

    @POST("v1/favorites")
    suspend fun addFavorite(@Body body: Map<String, Long>)

    @DELETE("v1/favorites")
    suspend fun removeFavorite(@Query ("user_id") userId: Long, @Query ("series_id") seriesId: Long)

}