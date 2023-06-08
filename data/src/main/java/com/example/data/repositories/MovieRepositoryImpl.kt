package com.example.data.repositories

import com.example.data.cache.Cache
import com.example.data.cache.model.mappers.toEntity
import com.example.data.network.API_KEY
import com.example.data.network.MovieApi
import com.example.data.network.model.mappers.toDomain
import com.example.domain.model.movie.Movie
import com.example.domain.repositories.MovieRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.data.cache.model.mappers.toDomain as cacheToDomain

@ActivityRetainedScoped
class MovieRepositoryImpl @Inject constructor(
    private val network: MovieApi,
    private val cache: Cache
) : MovieRepository.Remote, MovieRepository.Local {
    override fun getMovies(): Flow<List<Movie>> = flow {
        val movies = network.getNowPlayingMovies(API_KEY).results.map { it.toDomain() }
        emit(movies)
    }

    override fun getCachedMovies(): Flow<List<Movie>> =
        cache.getMovies().map { list -> (list.map { it.cacheToDomain() }) }

    override fun storeMovies(movies: List<Movie>) =
        cache.storeMovies(movies.map { it.toEntity() }.toTypedArray())

    override fun updateMovie(movie: Movie) = cache.updateMovie(movie.toEntity())
}