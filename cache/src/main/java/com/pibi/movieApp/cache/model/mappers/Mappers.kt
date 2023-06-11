package com.pibi.movieApp.cache.model.mappers

import com.pibi.movieApp.cache.model.CachedMovie
import com.example.domain.model.movie.Movie

fun CachedMovie.toDomain() =
    Movie(
        adult = adult,
        backdropPath = backdropPath,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isFavorite = isFavorite
    )

fun Movie.toEntity() =
    CachedMovie(
        adult = adult,
        backdropPath = backdropPath,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        isFavorite = isFavorite
    )