package com.example.domain.model.pagination

import com.example.domain.model.movie.Movie

data class PaginatedMovies(
    val movies: List<Movie>,
    val pagination: Pagination
)
