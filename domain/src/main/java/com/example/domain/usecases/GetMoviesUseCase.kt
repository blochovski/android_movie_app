package com.example.domain.usecases

import com.example.domain.repositories.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository.Remote
) {
    operator fun invoke() = movieRepository.getMovies()
}