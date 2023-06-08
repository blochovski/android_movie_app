package com.example.domain.usecases

import com.example.domain.model.pagination.Pagination
import com.example.domain.model.pagination.Pagination.Companion.PAGE_SIZE
import com.example.domain.repositories.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNextPageOfMovies @Inject constructor(
    private val movieRemoteRepository: MovieRepository.Remote,
    private val movieLocalRepository: MovieRepository.Local
) {
    operator fun invoke(pageToLoad: Int, pageSize: Int = PAGE_SIZE): Flow<Pagination> =
        movieRemoteRepository.getNextMoviesPage(
            pageToLoad = pageToLoad, numberOfItems = pageSize
        ).map {
            val (movies, pagination) = it
            movieLocalRepository.storeMovies(movies)
            pagination
        }
}