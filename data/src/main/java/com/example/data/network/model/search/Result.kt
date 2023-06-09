package com.example.data.network.model.search


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "adult")
    val adult: Boolean,
    @Json(name = "backdrop_path")
    val backdropPath: String? = String(),
    @Json(name = "id")
    val id: Int,
    @Json(name = "original_language")
    val originalLanguage: String? = String(),
    @Json(name = "original_title")
    val originalTitle: String? = String(),
    @Json(name = "overview")
    val overview: String? = String(),
    @Json(name = "popularity")
    val popularity: Double,
    @Json(name = "poster_path")
    val posterPath: String? = String(),
    @Json(name = "release_date")
    val releaseDate: String? = String(),
    @Json(name = "title")
    val title: String? = String(),
    @Json(name = "video")
    val video: Boolean,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int
)