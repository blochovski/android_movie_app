package com.pibi.movieApp.cache

import com.example.data.cache.Cache
import com.pibi.movieApp.cache.daos.MoviesDao
import com.pibi.movieApp.cache.model.mappers.toDomain
import com.pibi.movieApp.cache.model.mappers.toEntity
import com.example.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val movieDao: MoviesDao
) : Cache {

    override fun getMovies(): Flow<List<Movie>> =
        movieDao.getAll().map { movies -> movies.map { it.toDomain() } }

    override fun updateMovie(movie: Movie) = movieDao.updateMovie(movie.toEntity())

    override fun storeMovies(movies: Array<Movie>) =
        movieDao.insert(*movies.map { it.toEntity() }.toTypedArray())
}