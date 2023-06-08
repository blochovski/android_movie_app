package com.example.domain.usecases

import com.example.domain.model.movie.Movie
import com.example.domain.repositories.MovieRepository
import javax.inject.Inject

class UpdateMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository.Local
) {
    operator fun invoke(movie: Movie) = movieRepository.updateMovie(movie)
}