package com.pibi.movieApp.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pibi.movieApp.cache.daos.MoviesDao
import com.pibi.movieApp.cache.model.CachedMovie

@Database(
    entities = [
        CachedMovie::class,
    ],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}