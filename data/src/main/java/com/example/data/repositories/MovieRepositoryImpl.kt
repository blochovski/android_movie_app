package com.example.data.repositories

import com.example.data.MovieApi
import com.example.data.network.model.API_KEY
import com.example.data.network.model.mappers.toDomain
import com.example.domain.model.movie.Movie
import com.example.domain.repositories.MovieRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityRetainedScoped
class MovieRepositoryImpl @Inject constructor(
    private val network: MovieApi
) : MovieRepository {
    override fun getMovies(): Flow<List<Movie>> = flow {

        emit(network.getNowPlayingMovies(API_KEY).results.map { it.toDomain() })
    }
}