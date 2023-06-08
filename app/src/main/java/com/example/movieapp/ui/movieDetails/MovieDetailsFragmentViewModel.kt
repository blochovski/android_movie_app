package com.example.movieapp.ui.movieDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.domain.model.movie.EmptyMovie
import com.example.domain.model.movie.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailsFragmentViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val passedMovie: Movie = savedStateHandle.get<Movie>("movie") ?: EmptyMovie
    val movie: LiveData<Movie> get() = _movie
    private val _movie = MutableLiveData<Movie>()

    init {
        _movie.postValue(passedMovie)
    }
}