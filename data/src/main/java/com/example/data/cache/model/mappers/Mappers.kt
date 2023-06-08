package com.example.data.cache.model.mappers

import com.example.data.cache.model.CachedMovie
import com.example.domain.model.movie.Movie

fun CachedMovie.toDomain() =
    Movie(
        adult = adult,
        backdropPath = backdropPath,
//        genreIds = genreIds,
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
//        genreIds = genreIds,
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