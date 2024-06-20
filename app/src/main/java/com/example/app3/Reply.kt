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