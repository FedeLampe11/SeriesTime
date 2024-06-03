package com.example.app3

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/*
    "id": 35624,
    "name": "The Flash",
    "permalink": "the-flash",
    "start_date": "2014-10-07",
    "end_date": null,
    "country": "US",
    "network": "The CW",
    "status": "Ended",
    "image_thumbnail_path": "https://static.episodate.com/images/tv-show/thumbnail/35624.jpg"
*/
@Parcelize
data class Series(
    val id: Long,
    val name: String,
    val permalink: String?,
    val start_date: String?,
    val end_date: String?,
    val country: String?,
    val network: String?,
    val status: String?,
    val image_thumbnail_path: String?
): Parcelable