package com.example.data.network

import com.example.domain.model.movie.Movie
import com.example.domain.model.pagination.PaginatedMovies
import kotlinx.coroutines.flow.Flow

interface Network {

    fun getMovies(): Flow<List<Movie>>

    fun getNowPlayingMovies(
        pageToLoad: Int,
    ): Flow<PaginatedMovies>

    fun searchMovies(query: String, page: Int): Flow<PaginatedMovies>
}