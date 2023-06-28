package com.example.data.network

import com.example.domain.model.pagination.PaginatedMovies
import kotlinx.coroutines.flow.Flow

interface Network {

    fun getNowPlayingMoviesPage(
        pageToLoad: Int,
    ): Flow<PaginatedMovies>

    fun searchMovies(query: String, page: Int): Flow<PaginatedMovies>
}