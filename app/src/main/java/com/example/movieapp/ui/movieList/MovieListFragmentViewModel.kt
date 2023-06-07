package com.example.movieapp.ui.movieList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.movie.Movie
import com.example.domain.usecases.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListFragmentViewModel
@Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    val movies: LiveData<List<Movie>> get() = _movies
    private val _movies = MutableLiveData<List<Movie>>()

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch(IO) {
            _movies.postValue(getMoviesUseCase().first())
        }
    }
}