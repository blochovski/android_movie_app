package com.pibi.movieApp.network.model.search.mappers

import com.pibi.movieApp.network.model.search.MovieSearchResponse
import com.example.domain.model.movie.Movie
import com.example.domain.model.pagination.PaginatedMovies
import com.example.domain.model.pagination.Pagination
import com.pibi.movieApp.network.model.search.Result

fun Result.toDomain() = Movie(
    adult = adult,
    backdropPath = backdropPath ?: String(),
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


fun MovieSearchResponse.toDomainPaginatedMovies() = PaginatedMovies(
    movies = results.map { it.toDomain() }, pagination = Pagination(
        currentPage = page, totalPages = totalPages
    )
)