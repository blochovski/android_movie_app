package com.pibi.movieApp.network

import com.example.data.network.Network
import com.example.domain.model.pagination.PaginatedMovies
import com.pibi.movieApp.network.model.movies.mappers.toDomainPaginatedMovies
import com.pibi.movieApp.network.model.search.mappers.toDomainPaginatedMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetrofitNetwork @Inject constructor(
    private val api: MovieApi
) : Network {

    override fun getNowPlayingMoviesPage(pageToLoad: Int): Flow<PaginatedMovies> =
        flow {
            emit(
                api.getNowPlayingMovies(
                    apiKey = API_KEY,
                    pageToLoad = pageToLoad
                ).toDomainPaginatedMovies()
            )
        }

    override fun searchMovies(
        query: String,
        page: Int,
    ): Flow<PaginatedMovies> = flow {
        val response = api.searchMovies(
            apiKey = API_KEY,
            query = query,
            page = page
        )
        emit(response.toDomainPaginatedMovies())
    }
}