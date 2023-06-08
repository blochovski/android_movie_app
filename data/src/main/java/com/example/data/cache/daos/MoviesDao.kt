package com.example.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.cache.model.CachedMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg movies: CachedMovie)

    @Update
    fun updateMovie(movie: CachedMovie)

    @Query("SELECT * FROM movies")
    fun getAll(): Flow<List<CachedMovie>>
}