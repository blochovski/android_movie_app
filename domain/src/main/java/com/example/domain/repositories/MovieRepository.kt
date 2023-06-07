package com.example.domain.repositories

import com.example.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMovies(): Flow<List<Movie>>
}