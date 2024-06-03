package com.example.app3

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
* "id": 35624,
      "name": "The Flash",
      "permalink": "the-flash",
      "start_date": "2014-10-07",
      "end_date": null,
      "country": "US",
      "network": "The CW",
      "status": "Ended",
      "image_thumbnail_path": "https://static.episodate.com/images/tv-show/thumbnail/35624.jpg"*/
@Parcelize
data class Category(
    val total: String,
    val page: Int,
    val pages: Int,
    val tv_shows: List<Movie>
): Parcelable

data class CategoriesResponse(val categories: Category)