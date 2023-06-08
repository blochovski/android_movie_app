package com.example.domain.model.movie

import java.io.Serializable

open class Movie(
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
) : Serializable

object EmptyMovie : Movie(
    adult = false,
    backdropPath = String(),
    genreIds = listOf(),
    id = 0,
    originalLanguage = String(),
    originalTitle = String(),
    overview = String(),
    popularity = 0.0,
    posterPath = String(),
    releaseDate = String(),
    title = String(),
    video = false,
    voteAverage = 0.0,
    voteCount = 0
)