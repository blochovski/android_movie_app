package com.example.data.network

import com.example.data.network.model.movies.MovieNowPlayingResponse
import com.example.data.network.model.search.MovieSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(ApiConstants.NOW_PLAYING_MOVIE_ENDPOINT)
    suspend fun getNowPlayingMovies(
        @Query(ApiParameters.PAGE) pageToLoad: Int,
        @Query(ApiParameters.API_KEY) apiKey: String = API_KEY,
    ): MovieNowPlayingResponse

    @GET(ApiConstants.SEARCH_MOVIE_ENDPOINT)
    suspend fun searchMovies(
        @Query(ApiParameters.QUERY) query: String,
        @Query(ApiParameters.PAGE) page: Int,
        @Query(ApiParameters.API_KEY) apiKey: String = API_KEY,
    ): MovieSearchResponse
}