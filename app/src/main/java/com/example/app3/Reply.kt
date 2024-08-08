package com.example.app3

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Episode(
    val season: Int,
    val episode: Int,
    val name: String,
    val air_date: String
): Parcelable

@Parcelize
data class Details(
    val id: String,
    val name: String?,
    val description: String?,
    val startDate: String?,
    val status: String?,
    val network: String?,
    val thumbnail: String?,
    val rating: String?,
    val genres: List<String>?,
    val countdown: Episode?,
    val episodes: List<Episode>?
): Parcelable

@Parcelize
data class UserAuthReply(
    val id: Long,
    val full_name: String,
    val email: String,
    val password: String?,
    val meta_api_key: String?,
    val google_api_key: String?,
    val profile_picture: String?
): Parcelable

@Parcelize
data class EpisodeReply(
    val season: Int,
    val episode: Int
): Parcelable