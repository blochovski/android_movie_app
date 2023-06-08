package com.example.data.network.model.mappers

import com.example.data.network.model.MovieNowPlayingResponse
import com.example.data.network.model.Result
import com.example.domain.model.movie.Movie
import com.example.domain.model.pagination.PaginatedMovies
import com.example.domain.model.pagination.Pagination

fun Result.toDomain() = Movie(
    adult = adult,
    backdropPath = backdropPath ?: String(),
//        genreIds = genreIds,
    id = id,
    originalLanguage = originalLanguage ?: String(),
    originalTitle = originalTitle ?: String(),
    overview = overview ?: String(),
    popularity = popularity,
    posterPath = posterPath ?: String(),
    releaseDate = releaseDate ?: String(),
    title = title ?: String(),
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun MovieNowPlayingResponse.toDomainPaginatedMovies() = PaginatedMovies(
    movies = results.map { it.toDomain() }, pagination = Pagination(
        currentPage = page, totalPages = totalPages
    )
)

