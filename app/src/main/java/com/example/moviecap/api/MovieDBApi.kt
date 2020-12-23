package com.example.moviecap.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDBApi {

    companion object {
        private const val movieDBUrl = "https://api.themoviedb.org/"

        fun createApi() : MovieDBApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            val movieDBApi = Retrofit.Builder()
                .baseUrl(movieDBUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return movieDBApi.create(MovieDBApiService::class.java)
        }
    }
}