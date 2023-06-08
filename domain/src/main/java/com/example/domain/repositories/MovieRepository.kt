package com.example.domain.repositories

import com.example.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    interface Remote {
        fun getMovies(): Flow<List<Movie>>
    }
    interface Local  {
        fun getCachedMovies(): Flow<List<Movie>>

        fun storeMovies(movies: List<Movie>)

        fun updateMovie(movie: Movie)
    }
}