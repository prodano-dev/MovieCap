package com.example.moviecap.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.moviecap.dao.MovieDao
import com.example.moviecap.database.SavedMovieRoomDatabase
import com.example.moviecap.model.SavedMovie

class SavedMovieRepository(context: Context) {

    private var movieDao: MovieDao

    init {
        val savedMovieRoomDatabase = SavedMovieRoomDatabase.getDatabase(context)
        movieDao = savedMovieRoomDatabase!!.movieDao()
    }

    fun getAllSavedMovies(): LiveData<List<SavedMovie>> {
        return movieDao.getAllSavedMovies()
    }

    fun addMovie(movie: SavedMovie){
        movieDao.addMovieToList(movie)
    }

    class SaveMovieFetchError(message: String, cause: Throwable) : Throwable(message, cause)
}