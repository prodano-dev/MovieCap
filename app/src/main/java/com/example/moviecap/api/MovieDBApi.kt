package com.example.moviecap.api

import com.example.moviecap.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDBApi {

    companion object {
        private const val movieDBUrl = "https://api.themoviedb.org/"

        fun createApi() : MovieDBApiService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(ApiInterceptor())
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

class ApiInterceptor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val url: HttpUrl = request.url.newBuilder().addQueryParameter("api_key", BuildConfig.MOVIEDB_KEY).build()
        request = request.newBuilder().url(url).build()

        return chain.proceed(request)
    }
}