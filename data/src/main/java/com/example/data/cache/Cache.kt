package com.example.data.cache

import com.example.data.cache.model.CachedMovie
import kotlinx.coroutines.flow.Flow

interface Cache {

    fun getMovies(): Flow<List<CachedMovie>>

    fun updateMovie(movie: CachedMovie)

    fun storeMovies(movies: Array<CachedMovie>)
}