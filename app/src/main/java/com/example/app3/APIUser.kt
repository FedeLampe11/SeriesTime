package com.example.app3

import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("http://kotlin-backend-env.eba-p3ttnvax.eu-north-1.elasticbeanstalk.com/")
    .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
    .build()

val userService: APIUser = retrofit.create(APIUser::class.java)

interface APIUser {
    @POST("v1/users")
    suspend fun postNewUser(@Body body: Map<String, String?>): UserAuthReply

    @GET("v1/users")
    suspend fun loginUser(@Query ("email") email: String, @Query("password") password: String): UserAuthReply

    @POST("v1/users/image")
    suspend fun updateProfilePicture(@Body body: Map<String, String>)

    @GET("v1/favorites")
    suspend fun getFavourite(@Query ("user_id") userId: Long): List<Details>

    @POST("v1/favorites")
    suspend fun addFavorite(@Body body: Map<String, Long>)

    @DELETE("v1/favorites")
    suspend fun removeFavorite(@Query ("user_id") userId: Long, @Query ("series_id") seriesId: Long)

    @GET("v1/series")
    suspend fun getDetailPage(@Query("series_id") id: Long): Details

    @GET("v1/series/search")
    suspend fun getSearchPage(@Query("q") query: String): List<Details>

    @GET("v1/series/most_popular")
    suspend fun getMostPopular(): List<Details>

    @GET("v1/watched")
    suspend fun getWatchedEpisodes(@Query ("user_id") userId: Long, @Query ("series_id") seriesId: Long): List<EpisodeReply>

    @POST("v1/watched")
    suspend fun addWatchedEpisode(@Body body: Map<String, Long>)

    @DELETE("v1/watched")
    suspend fun removeWatchedEpisode(@Query ("user_id") userId: Long, @Query ("series_id") seriesId: Long, @Query ("season") season: Long, @Query ("episode") episode: Long)
}