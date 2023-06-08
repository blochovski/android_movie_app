package com.example.domain.usecases

import com.example.domain.repositories.MovieRepository
import javax.inject.Inject

class GetCachedMoviesUseCase  @Inject constructor(
    private val movieRepository: MovieRepository.Local
) {
    operator fun invoke() = movieRepository.getCachedMovies()
}