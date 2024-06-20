package com.example.app3

data class Movie(
    val id: Long,
    val name: String,
    val permalink: String?,
    val start_date: String?,
    val end_date: String?,
    val country: String?,
    val network: String?,
    val status: String?,
    val image_thumbnail_path: String?
)

