package com.example.movieapp.ui.movieList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.movie.Movie
import com.example.domain.model.pagination.Pagination
import com.example.domain.usecases.GetCachedMoviesUseCase
import com.example.domain.usecases.GetNextPageOfMovies
import com.example.domain.usecases.SearchMoviesRemotelyUseCase
import com.example.domain.usecases.UpdateMovieUseCase
import com.example.exceptions.NoMoreMoviesException
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Error
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Loaded
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Loading
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Searching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListFragmentViewModel
@Inject constructor(
    private val getNextPageOfMovies: GetNextPageOfMovies,
    private val getCachedMoviesUseCase: GetCachedMoviesUseCase,
    private val updateMovieUseCase: UpdateMovieUseCase,
    private val searchMoviesRemotelyUseCase: SearchMoviesRemotelyUseCase
) : ViewModel() {

    val state: LiveData<MovieListFragmentUiState> get() = _state
    private val _state = MutableLiveData<MovieListFragmentUiState>()
    private var movies = listOf<Movie>()

    private var currentPage = 0
    var isLastPage = false
    var isLoadingMoreMovies: Boolean = false

    private var runningJobs = mutableListOf<Job>()
    private var query: String = String()

    init {
        loadNextMoviesPage(isLoadingMoreMovies = false)
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

    private fun onRemoteSearchResults(localMovies: List<Movie>) {
        val currentList = movies
        val newMovies = localMovies.subtract(currentList.toSet())
        val updatedList = currentList + newMovies
        movies = updatedList
        _state.postValue(
            Searching(
                updatedList,
            )
        )
    }

    private fun onError(error: Throwable) {
        if (error is NoMoreMoviesException)
            isLoadingMoreMovies = false
        isLastPage = true
        _state.postValue(
            Error(error)
        )
    }

    fun loadNextMoviesPage(isLoadingMoreMovies: Boolean) {
        _state.postValue(Loading)
        this@MovieListFragmentViewModel.isLoadingMoreMovies = isLoadingMoreMovies
        val job = viewModelScope.launch(IO) {

            runningJobs.filterNot { it == this.coroutineContext.job }.onEach {
                it.cancel()
            }
            combine(
                getCachedMoviesUseCase(),
                getNextPageOfMovies(++currentPage)
            ) { cached, paginated ->
                initIsFavoriteProperty(cached, paginated.movies)
                paginated
            }.catch { error ->
                onError(error)
            }.collect {
                val (movies, pagination) = it
                onNewMoviesList(movies)
                updatePaginationInfo(pagination)
                this@MovieListFragmentViewModel.isLoadingMoreMovies = false
            }
        }

        runningJobs.add(job)
        job.invokeOnCompletion {
            it?.printStackTrace()
            runningJobs.remove(job)
        }
    }

    private fun initIsFavoriteProperty(cached: List<Movie>, remote: List<Movie>): List<Movie> {
        val cachedIds = cached.map { it.id }
        remote.map { movie ->
            if (movie.id in cachedIds) movie.apply {
                isFavorite = cached.first { id == it.id }.isFavorite
            }
        }
        return remote
    }


    fun searchRemotelyNextMoviesPage() {
        searchRemotely(query, isLoadingMoreMovies = true)
    }

    private fun searchRemotely(query: String, isLoadingMoreMovies: Boolean) {
        _state.postValue(Loading)
        this@MovieListFragmentViewModel.isLoadingMoreMovies = isLoadingMoreMovies
        val job = viewModelScope.launch(IO) {

            runningJobs.filterNot { it == this.coroutineContext.job }.onEach {
                it.cancel()
            }
            combine(
                getCachedMoviesUseCase(),
                searchMoviesRemotelyUseCase(
                    pageToLoad = ++currentPage, query = query
                )
            ) { cached, paginated ->
                initIsFavoriteProperty(cached, paginated.movies)
                paginated
            }.catch { error ->
                onError(error)
            }.collect {
                val (movies, pagination) = it
                onRemoteSearchResults(movies)
                updatePaginationInfo(pagination)
                this@MovieListFragmentViewModel.isLoadingMoreMovies = false
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
        searchRemotely(query = suggestion, isLoadingMoreMovies = false)
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
            loadNextMoviesPage(isLoadingMoreMovies = false)
            this@MovieListFragmentViewModel.query = query
            return
        }

        if (this@MovieListFragmentViewModel.query != query) {
            movies = listOf()
            resetPagination()
            searchRemotely(query, isLoadingMoreMovies = false)
        } else {
            searchRemotely(query, isLoadingMoreMovies = true)
        }

        this@MovieListFragmentViewModel.query = query
    }

    private fun resetPagination() {
        currentPage = 0
        isLastPage = false
    }
}