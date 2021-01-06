package com.example.moviecap.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.moviecap.model.SavedMovie
import com.example.moviecap.repository.SavedMovieRepository
import kotlinx.coroutines.launch

class SelectedMovieViewModel(application: Application): AndroidViewModel(application) {

    private val savedMovieRepository = SavedMovieRepository(application.applicationContext)
    val savedMovies: LiveData<List<SavedMovie>> = savedMovieRepository.getAllSavedMovies()
    var savedMoviesArray: ArrayList<SavedMovie> = arrayListOf()
    val ratedMovies = savedMovieRepository.getRatedMovies()
    val watchList = savedMovieRepository.getWatchList()
    val myOwnMovies = savedMovieRepository.getOwnMovies()
    var selectedMovie: SavedMovie? = null
    val feedBackString = MutableLiveData<String>()

    fun addMovieToList(movie: SavedMovie) {
        viewModelScope.launch {
            try {
                savedMovieRepository.addMovie(movie)
                feedBackString.value = "Successfully added ${movie.title} to the list"
            } catch (error: SavedMovieRepository.SaveMovieFetchError) {
                Log.e("add went wrong..", error.cause.toString())
                feedBackString.value = error.cause.toString()
            }
        }
    }

    fun isInWatchList() :Boolean {
        for (movie in savedMoviesArray) {
            if (movie.movieId == selectedMovie!!.movieId) {
                return true
            }
        }
        return false
    }

    fun hasBeenRated() : Boolean {

        return selectedMovie!!.ratings != null
    }

    fun setDatabaseIdForMovie() {
        for (movie in savedMoviesArray) {
            if (movie.movieId == selectedMovie!!.movieId) {
               selectedMovie!!.id = movie.id
                selectedMovie!!.ratings = movie.ratings
            }
        }
    }

    fun changeRatings(rate: Double) {
        for (movie in savedMoviesArray) {
            if (movie.movieId == selectedMovie!!.movieId) {
                 movie.ratings = rate
            }
        }
    }

    fun removeAllMovies(){
        viewModelScope.launch {
            try {
                savedMovieRepository.deleteAll()

            } catch (error: SavedMovieRepository.SaveMovieFetchError) {
                Log.e("Remove", error.cause.toString())
            }
        }
    }

    fun updateMovie(movie: SavedMovie) {
        viewModelScope.launch {
            try {
                savedMovieRepository.updateMovie(movie)
                feedBackString.value = "Successfully updated movie"
            } catch (error: SavedMovieRepository.SaveMovieFetchError) {
                Log.e("update", error.cause.toString())
            }
        }
    }

    fun removieMovieFromList(movie: SavedMovie) {
        viewModelScope.launch {
            try {
                savedMovieRepository.deleteMovie(movie)
                feedBackString.value = "Successfully removed ${movie.title}"
            } catch (error: SavedMovieRepository.SaveMovieFetchError) {
                Log.e("Remove", error.cause.toString())
                feedBackString.value = error.cause.toString()
            }
        }
    }

}