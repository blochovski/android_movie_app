package com.example.movieapp.core.di

import com.example.data.repositories.MovieRepositoryImpl
import com.example.domain.repositories.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ViewModelsModule {

    @Binds
    abstract fun bindMovieRepository(repository: MovieRepositoryImpl): MovieRepository
}