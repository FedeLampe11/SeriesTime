package com.example.app3

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Series(
    val id: String,
    val name: String?,
    val permalink: String?,
    val start_date: String?,
    val end_date: String?,
    val country: String?,
    val network: String?,
    val status: String?,
    val image_thumbnail_path: String?,
): Parcelable

@Parcelize
data class Reply(
    val total: String,
    val page: Int,
    val pages: Int,
    val tv_shows: List<Series>,
): Parcelable

@Parcelize
data class Episode(
    val season: Int,
    val episode: Int,
    val epName: String,
    val airDate: String
): Parcelable

@Parcelize
data class Details(
    val id: String,
    val name: String?,
    val permalink: String?,
    val url: String?,
    val description: String?,
    val descriptionSource: String?,
    val start_date: String?,
    val end_date: String?,
    val country: String?,
    val status: String?,
    val runtime: String?,
    val network: String?,
    val youtube_link: String?,
    val image_path: String?,
    val image_thumbnail_path: String?,
    val rating: String?,
    val rating_count: String?,
    val countdown: String?,
    val genres: List<String>?,
    val pictures: List<String>?,
    val episodes: List<Episode>?
): Parcelable

@Parcelize
data class EpReply(
    val tvShow: Details
): Parcelable