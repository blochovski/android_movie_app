package com.example.domain.usecases

import com.example.domain.model.pagination.PaginatedMovies
import com.example.domain.repositories.MovieRepository
import com.example.exceptions.NoMoreMoviesException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchMoviesRemotelyUseCase @Inject constructor(
    private val movieRemoteRepository: MovieRepository.Remote,
    private val movieLocalRepository: MovieRepository.Local
) {
    operator fun invoke(
        pageToLoad: Int,
        query: String,
    ): Flow<PaginatedMovies> =
        movieRemoteRepository.searchMoviesRemotely(
            pageToLoad = pageToLoad,
            query = query
        ).map { paginatedMovies ->
            val movies = paginatedMovies.movies
            if (movies.isEmpty()) throw NoMoreMoviesException("No more movies")
            movieLocalRepository.storeMovies(movies)
            paginatedMovies
        }
}