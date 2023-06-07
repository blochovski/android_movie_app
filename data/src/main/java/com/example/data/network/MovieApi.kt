package com.example.data

import com.example.data.network.model.API_KEY
import com.example.data.network.model.ApiConstants
import com.example.data.network.model.ApiParameters
import com.example.data.network.model.MovieNowPlayingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(ApiConstants.NOW_PLAYING_MOVIE_ENDPOINT)
    suspend fun getNowPlayingMovies(
        @Query(ApiParameters.API_KEY) pageToLoad: String = API_KEY,
    ): MovieNowPlayingResponse
}