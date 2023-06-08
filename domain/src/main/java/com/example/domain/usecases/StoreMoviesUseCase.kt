package com.example.domain.usecases

import com.example.domain.model.movie.Movie
import com.example.domain.repositories.MovieRepository
import javax.inject.Inject

class StoreMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository.Local
) {
    operator fun invoke(movies: List<Movie>) = movieRepository.storeMovies(movies)
}