package com.example.movieapp.ui.movieList

import com.example.domain.model.movie.Movie

sealed interface MovieListFragmentUiState {

    object Loading : MovieListFragmentUiState

    class Loaded(
        val movies: List<Movie> = emptyList(),
    ) : MovieListFragmentUiState

    class Error(val error: Throwable) : MovieListFragmentUiState
}