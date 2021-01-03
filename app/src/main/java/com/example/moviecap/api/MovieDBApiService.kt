package com.example.moviecap.api

import android.os.Build
import com.example.moviecap.BuildConfig
import com.example.moviecap.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBApiService {

    @GET("3/movie/upcoming?")
    suspend fun getUpcomingMovies() : ApiResponse.Result

    @GET("3/movie/now_playing?")
    suspend fun getNowPlayingMovies() : ApiResponse.Result

    @GET("3/movie/top_rated?")
    suspend fun getTopRatedMovies() : ApiResponse.Result

    @GET("/3/search/movie?")
    suspend fun getMoviesWithQuery(@Query("query") query: String): ApiResponse.Result

    @GET("3/movie/{movie_id}/videos?")
    suspend fun getTrailerOfMovie(@Path("movie_id") movie_id: Int) : ApiResponse.TrailerResult

}
