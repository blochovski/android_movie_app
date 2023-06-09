package com.pibi.movieApp.cache.di

import android.content.Context
import androidx.room.Room
import com.example.data.cache.Cache
import com.pibi.movieApp.cache.Database
import com.pibi.movieApp.cache.RoomCache
import com.pibi.movieApp.cache.daos.MoviesDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {

        @Provides
        fun provideDatabase(@ApplicationContext context: Context): Database {
            return Room.databaseBuilder(context, Database::class.java, "movies.db")
                .build()
        }

        @Provides
        fun provideMoviesDao(moviesDatabase: Database): MoviesDao =
            moviesDatabase.moviesDao()
    }
}