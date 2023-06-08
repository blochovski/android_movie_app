package com.example.movieapp.ui.movieDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.movie.EmptyMovie
import com.example.domain.model.movie.Movie
import com.example.domain.usecases.UpdateMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsFragmentViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateMovieUseCase: UpdateMovieUseCase
) : ViewModel() {
    private val passedMovie: Movie = savedStateHandle.get<Movie>("movie") ?: EmptyMovie
    val movie: LiveData<Movie> get() = _movie
    private val _movie = MutableLiveData<Movie>()

    init {
        _movie.postValue(passedMovie)
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            updateMovieUseCase(movie)
        }
    }
}