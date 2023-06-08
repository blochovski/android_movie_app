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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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

    val movies: LiveData<List<Movie>> get() = _movies
    private val _movies = MutableLiveData<List<Movie>>()

    private var currentPage = 0
    var isLastPage = false
    var isLoadingMoreMovies: Boolean = false

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch(IO) {
            val allMovies = combine(
                getCachedMoviesUseCase(),
                getMoviesUseCase()
            ) { cached, remote ->
                val movies: MutableList<Movie> = mutableListOf()
                with(movies) {
                    val idsOfCached = cached.map { it.id }
                    val newMovies = remote.filter { movie -> movie.id !in idsOfCached }
                    storeMoviesUseCase(newMovies)
                    addAll(newMovies)
                    addAll(cached)
                    sortBy { it.id }
                    toList()
                }
            }.first()
            _movies.postValue(allMovies)
        }
    }

    fun loadNextMoviesPage() {

        viewModelScope.launch(IO) {
            val pagination = getNextPageOfMovies(++currentPage)
            updatePaginationInfo(pagination.first())
            isLoadingMoreMovies = false
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