package com.example.movieapp.ui.movieList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.movie.Movie
import com.example.domain.model.pagination.Pagination
import com.example.domain.usecases.GetCachedMoviesUseCase
import com.example.domain.usecases.GetMoviesUseCase
import com.example.domain.usecases.GetNextPageOfMovies
import com.example.domain.usecases.StoreMoviesUseCase
import com.example.domain.usecases.UpdateMovieUseCase
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Error
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Loaded
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListFragmentViewModel
@Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getNextPageOfMovies: GetNextPageOfMovies,
    private val getCachedMoviesUseCase: GetCachedMoviesUseCase,
    private val storeMoviesUseCase: StoreMoviesUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase
) : ViewModel() {

    val state: LiveData<MovieListFragmentUiState> get() = _state
    private val _state = MutableLiveData<MovieListFragmentUiState>()

    private var currentPage = 0
    private var movies: List<Movie> = listOf()
    var isLastPage = false
    var isLoadingMoreMovies: Boolean = false

    init {
        _state.value = Loading
        collectMoviesUpdates()
    }

    private fun collectMoviesUpdates() {
        viewModelScope.launch(IO) {
            _state.postValue(Loading)
            combine(
                getCachedMoviesUseCase(),
                getMoviesUseCase()
            ) { cached, remote ->
                storeMoviesUseCase(remote)
                val cachedIds = cached.map { it.id }
                remote.map { movie ->
                    if (movie.id in cachedIds) movie.apply {
                        isFavorite = cached.first { id == it.id }.isFavorite
                    }
                }
                remote
            }
                .catch { error ->
                    onError(error)
                }
                .collect { movies ->
                    onNewMoviesList(movies)
                }
        }
    }

    private fun onNewMoviesList(localMovies: List<Movie>) {
        val currentList = movies
        val newMovies = localMovies.subtract(currentList.toSet())
        val updatedList = currentList + newMovies
        movies = updatedList
        _state.postValue(
            Loaded(
                updatedList,
            )
        )
    }

    private fun onError(error: Throwable) {
        _state.postValue(
            Error(error)
        )
    }

    fun loadNextMoviesPage() {
        _state.postValue(Loading)
        isLoadingMoreMovies = true
        viewModelScope.launch(IO) {
            getNextPageOfMovies(++currentPage).catch { error ->
                onError(error)
            }.collect {
                val (movies, pagination) = it
                onNewMoviesList(movies)
                updatePaginationInfo(pagination)
                isLoadingMoreMovies = false
            }
        }
    }

    private fun updatePaginationInfo(pagination: Pagination) {
        currentPage = pagination.currentPage
        isLastPage = !pagination.canFetchNextPage
    }

    fun updateMovie(movie: Movie) {
        viewModelScope.launch(IO) {
            updateMovieUseCase(movie)
        }
    }
}