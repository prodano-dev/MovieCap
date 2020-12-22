package com.example.moviecap.API

import com.example.moviecap.Model.ApiResponse
import retrofit2.http.GET

interface MovieDBApiService {

    @GET("3/movie/upcoming?api_key=e952a2ed50e8f0a42cda74061fc1d4a2&language=en-US&page=1")
    suspend fun getUpcomingMovies() : ApiResponse.Result

    @GET("3/movie/now_playing?api_key=e952a2ed50e8f0a42cda74061fc1d4a2&language=en-US&page=1")
    suspend fun getNowPlayingMovies() : ApiResponse.Result

    @GET("3/movie/top_rated?api_key=e952a2ed50e8f0a42cda74061fc1d4a2&language=en-US&page=1")
    suspend fun getTopRatedMovies() : ApiResponse.Result

}