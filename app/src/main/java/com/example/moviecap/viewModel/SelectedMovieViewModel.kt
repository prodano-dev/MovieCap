package com.example.moviecap.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviecap.model.SavedMovie
import com.example.moviecap.repository.SavedMovieRepository
import kotlinx.coroutines.launch

class SelectedMovieViewModel(application: Application): AndroidViewModel(application) {

    private val savedMovieRepository = SavedMovieRepository(application.applicationContext)
    val savedMovies: LiveData<List<SavedMovie>> = savedMovieRepository.getAllSavedMovies()
    var selectedMovie: SavedMovie? = null



    fun addMovieToList(movie: SavedMovie) {
        viewModelScope.launch {
            try {
                savedMovieRepository.addMovie(movie)
            } catch (error: SavedMovieRepository.SaveMovieFetchError) {
                Log.e("add went wrong..", error.cause.toString())
            }
        }
    }

}