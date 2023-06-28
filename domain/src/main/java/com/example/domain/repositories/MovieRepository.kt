package com.example.domain.repositories

import com.example.domain.model.movie.Movie
import com.example.domain.model.pagination.PaginatedMovies
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    interface Remote {

        fun getNowPlayingMoviesPage(
            pageToLoad: Int,
            numberOfItems: Int
        ): Flow<PaginatedMovies>

        fun searchMoviesRemotely(pageToLoad: Int, query: String): Flow<PaginatedMovies>
    }

    interface Local {
        fun getCachedMovies(): Flow<List<Movie>>

        fun storeMovies(movies: List<Movie>)

        fun updateCachedMovie(movie: Movie)
    }
}