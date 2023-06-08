package com.example.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.cache.daos.MoviesDao
import com.example.data.cache.model.CachedMovie

@Database(
    entities = [
        CachedMovie::class,
    ],
    version = 1
)
abstract class Database : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}