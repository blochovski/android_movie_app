package com.example.data.cache

import com.example.data.cache.daos.MoviesDao
import com.example.data.cache.model.CachedMovie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val movieDao: MoviesDao
) : Cache {

    override fun getMovies(): Flow<List<CachedMovie>> = movieDao.getAll()

    override fun updateMovie(movie: CachedMovie) = movieDao.updateMovie(movie)

    override fun storeMovies(movies: Array<CachedMovie>) = movieDao.insert(*movies)
}