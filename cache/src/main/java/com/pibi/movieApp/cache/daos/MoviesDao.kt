package com.pibi.movieApp.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.pibi.movieApp.cache.model.CachedMovie

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg movies: CachedMovie)

    @Update
    fun updateMovie(movie: CachedMovie)

    @Query("SELECT * FROM movies")
    fun getAll(): Flow<List<CachedMovie>>
}