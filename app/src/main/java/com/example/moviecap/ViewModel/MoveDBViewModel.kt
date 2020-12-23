package com.example.moviecap.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviecap.Repository.MovieDBRepository
import kotlinx.coroutines.launch

class MoveDBViewModel(application: Application): AndroidViewModel(application) {

    private val movieDBRepository = MovieDBRepository()
    val upcomingMovies = movieDBRepository.upcomingMovies
    val topRatedMovies = movieDBRepository.topRatedMovies
    val nowPlayingMovies = movieDBRepository.nowPlayingMovies
    val searchedMovies = movieDBRepository.searchedMovies

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            try {
                movieDBRepository.getNowPlayingMovies()
            } catch (error: MovieDBRepository.MovieFetchError) {
                Log.e("lol", error.cause.toString())
            }
        }
    }

    fun getTopRatedMovies() {
        viewModelScope.launch {
            try {
                movieDBRepository.getTopRatedMovies()
            } catch (error: MovieDBRepository.MovieFetchError) {
                Log.e("lol", error.cause.toString())
            }
        }
    }

    fun getUpcomingMovies() {
        viewModelScope.launch {
            try {
                movieDBRepository.getUpcomingMovies()
            } catch (error: MovieDBRepository.MovieFetchError) {
                Log.e("lol", error.cause.toString())
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                movieDBRepository.searchMoviesWithQuery(query)
            } catch (error: MovieDBRepository.MovieFetchError) {
                Log.e("something went wrong..", error.cause.toString())
            }
        }
    }
}