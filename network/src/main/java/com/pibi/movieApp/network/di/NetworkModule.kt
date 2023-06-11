package com.pibi.movieApp.network.di

import com.pibi.movieApp.network.ApiConstants
import com.pibi.movieApp.network.MovieApi
import com.example.data.network.Network
import com.pibi.movieApp.network.RetrofitNetwork
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkModule {

    @Binds
    abstract fun bindNetwork(network: RetrofitNetwork): Network

    companion object{
        @Provides
        @Singleton
        fun provideApi(okHttpClient: OkHttpClient): MovieApi {
            return Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(MovieApi::class.java)
        }

        @Provides
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .cache(null)
                .build()
        }
    }
}