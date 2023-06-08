package com.example.data.network

import com.example.data.network.model.MovieNowPlayingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(ApiConstants.NOW_PLAYING_MOVIE_ENDPOINT)
    suspend fun getNowPlayingMovies(
        @Query(ApiParameters.PAGE) pageToLoad: Int,
        @Query(ApiParameters.LIMIT) pageSize: Int,
        @Query(ApiParameters.API_KEY) apiKey: String = API_KEY,
    ): MovieNowPlayingResponse
}