package com.example.moviecap.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviecap.api.MovieDBApi
import com.example.moviecap.api.MovieDBApiService
import com.example.moviecap.model.ApiResponse
import com.example.moviecap.model.MovieDB
import com.example.moviecap.model.MovieTrailer
import kotlinx.coroutines.withTimeout

class MovieDBRepository {

    private val movieDBApiService: MovieDBApiService = MovieDBApi.createApi()

    private val _upcomingMovies: MutableLiveData<List<MovieDB>> = MutableLiveData()
    val upcomingMovies: LiveData<List<MovieDB>>
    get() = _upcomingMovies

    private val _nowPlayingMovies: MutableLiveData<List<MovieDB>> = MutableLiveData()
    val nowPlayingMovies: LiveData<List<MovieDB>>
        get() = _nowPlayingMovies

    private val _topRatedMovies: MutableLiveData<List<MovieDB>> = MutableLiveData()
    val topRatedMovies: LiveData<List<MovieDB>>
        get() = _topRatedMovies

    private val _searchedMovies: MutableLiveData<List<MovieDB>> = MutableLiveData()
    val searchedMovies: LiveData<List<MovieDB>>
        get() = _searchedMovies

    private val _movieTrailers: MutableLiveData<List<MovieTrailer>> = MutableLiveData()
    val movieTrailers: LiveData<List<MovieTrailer>>
        get() = _movieTrailers

    suspend fun getUpcomingMovies() {

        try {
            val result = withTimeout(5000) {
                movieDBApiService.getUpcomingMovies()
            }

            _upcomingMovies.value = result.results

        } catch (error: Throwable) {
            throw MovieFetchError("Cant fetch upcoming movies", error)
        }
    }

    suspend fun getTopRatedMovies() {

        try {
            val result = withTimeout(5000) {
                movieDBApiService.getTopRatedMovies()
            }

            _topRatedMovies.value = result.results

        } catch (error: Throwable) {
            throw MovieFetchError("Cant fetch top rated movies", error)
        }
    }

    suspend fun getNowPlayingMovies() {

        try {
            val result = withTimeout(5000) {
                movieDBApiService.getNowPlayingMovies()
            }

            _nowPlayingMovies.value = result.results

        } catch (error: Throwable) {
            throw MovieFetchError("Cant fetch now playing movies", error)
        }
    }

    suspend fun searchMoviesWithQuery(query: String) {

        try {
            val result: ApiResponse.Result = withTimeout(5000) {
                movieDBApiService.getMoviesWithQuery(query)
            }

            _searchedMovies.value = result.results
        } catch (error: Throwable) {
            throw MovieFetchError("Couldnt find the movie you looking for", error)
        }
    }

    suspend fun getTrailerOfMovies(movie_id: Int) {

        try {
            val result: ApiResponse.TrailerResult = withTimeout(5000) {
               movieDBApiService.getTrailerOfMovie(movie_id)
            }

            _movieTrailers.value = result.results
        } catch (error: Throwable) {
            throw MovieFetchError("Couldnt find the movie you looking for", error)
        }
    }

    class MovieFetchError(message: String, cause: Throwable) : Throwable(message, cause)
}