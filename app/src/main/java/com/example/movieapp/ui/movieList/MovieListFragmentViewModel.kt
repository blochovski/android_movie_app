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
import com.example.domain.usecases.SearchMoviesRemotelyUseCase
import com.example.domain.usecases.StoreMoviesUseCase
import com.example.domain.usecases.UpdateMovieUseCase
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Error
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Loaded
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Loading
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Searching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListFragmentViewModel
@Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getNextPageOfMovies: GetNextPageOfMovies,
    private val getCachedMoviesUseCase: GetCachedMoviesUseCase,
    private val storeMoviesUseCase: StoreMoviesUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val searchMoviesRemotelyUseCase: SearchMoviesRemotelyUseCase
) : ViewModel() {

    var suggestions = listOf<String>()
        private set
    val state: LiveData<MovieListFragmentUiState> get() = _state
    private val _state = MutableLiveData<MovieListFragmentUiState>()
    private var movies = listOf<Movie>()

    private var currentPage = 0
    var isLastPage = false
    var isLoadingMoreMovies: Boolean = false

    private var runningJobs = mutableListOf<Job>()
    private var query: String = String()

    init {
        _state.value = Loading
        collectMoviesUpdates()
    }

    private fun collectMoviesUpdates() {
        currentPage = 0
        viewModelScope.launch(IO) {
            _state.postValue(Loading)
            combine(
                getCachedMoviesUseCase(), getMoviesUseCase()
            ) { cached, remote ->
                storeMoviesUseCase(remote)
                val cachedIds = cached.map { it.id }
                remote.map { movie ->
                    if (movie.id in cachedIds) movie.apply {
                        isFavorite = cached.first { id == it.id }.isFavorite
                    }
                }
                remote
            }.catch { error ->
                onError(error)
            }.collect { movies ->
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

    fun searchRemotely() {
        searchRemotely(query)
    }

    private fun searchRemotely(query: String) {
        _state.postValue(Loading)
        isLoadingMoreMovies = true
        val job = viewModelScope.launch(IO) {
            searchMoviesRemotelyUseCase(
                pageToLoad = ++currentPage, query = query
            ).onEach {
                runningJobs.map { it.cancel() }
            }.catch { error ->
                onError(error)
            }.collect {
                val (movies, pagination) = it
                onRemoteSearchResults(movies)
                updatePaginationInfo(pagination)
                isLoadingMoreMovies = false
            }
        }

        runningJobs.add(job)
        job.invokeOnCompletion {
            it?.printStackTrace()
            runningJobs.remove(job)
        }
    }

    fun searchSuggestionRemotely(suggestion: String) {
        movies = emptyList()
        resetPagination()
        searchRemotely(query = suggestion)
    }

    private fun onRemoteSearchResults(localMovies: List<Movie>) {
        val currentList = movies
        val newMovies = localMovies.subtract(currentList.toSet())
        val updatedList = currentList + newMovies
        movies = updatedList
        suggestions = movies.map { it.title }
        _state.postValue(
            Searching(
                updatedList,
            )
        )
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

    fun updateQuery(query: String) {
        if (query.isEmpty()) {
            movies = listOf()
            resetPagination()
            collectMoviesUpdates()
            return
        }

        if (this@MovieListFragmentViewModel.query != query) {
            movies = listOf()
            resetPagination()
        }
        this@MovieListFragmentViewModel.query = query
        searchRemotely(query)
    }

    private fun resetPagination() {
        currentPage = 0
        isLastPage = false
    }
}