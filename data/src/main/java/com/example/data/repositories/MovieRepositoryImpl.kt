package com.example.data.repositories

import com.example.data.cache.Cache
import com.example.data.network.Network
import com.example.domain.model.movie.Movie
import com.example.domain.model.pagination.PaginatedMovies
import com.example.domain.repositories.MovieRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class MovieRepositoryImpl @Inject constructor(
    private val network: Network,
    private val cache: Cache
) : MovieRepository.Remote, MovieRepository.Local {

    override fun getNowPlayingMoviesPage(
        pageToLoad: Int,
        numberOfItems: Int
    ): Flow<PaginatedMovies> =
        network.getNowPlayingMoviesPage(pageToLoad = pageToLoad)

    override fun searchMoviesRemotely(
        pageToLoad: Int,
        query: String,
    ): Flow<PaginatedMovies> =
        network.searchMovies(page = pageToLoad, query = query)

    override fun getCachedMovies(): Flow<List<Movie>> =
        cache.getMovies().map { list -> (list.map { it }) }

    override fun storeMovies(movies: List<Movie>) =
        cache.storeMovies(movies.toTypedArray())

    override fun updateCachedMovie(movie: Movie) = cache.updateMovie(movie)
}