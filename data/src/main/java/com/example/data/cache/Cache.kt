package com.example.data.cache

import com.example.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface Cache {

    fun getMovies(): Flow<List<Movie>>

    fun updateMovie(movie: Movie)

    fun storeMovies(movies: Array<Movie>)
}