package com.example.moviecap.api

import com.example.moviecap.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDBApiService {

    @GET("3/movie/upcoming?api_key=e952a2ed50e8f0a42cda74061fc1d4a2&language=en-US&page=1")
    suspend fun getUpcomingMovies() : ApiResponse.Result

    @GET("3/movie/now_playing?api_key=e952a2ed50e8f0a42cda74061fc1d4a2&language=en-US&page=1")
    suspend fun getNowPlayingMovies() : ApiResponse.Result

    @GET("3/movie/top_rated?api_key=e952a2ed50e8f0a42cda74061fc1d4a2&language=en-US&page=1")
    suspend fun getTopRatedMovies() : ApiResponse.Result

    @GET("/3/search/movie?api_key=e952a2ed50e8f0a42cda74061fc1d4a2&language=en-US&query=harry&page=1&include_adult=false")
    suspend fun getMoviesWithQuery(@Query("query") query: String): ApiResponse.Result

}