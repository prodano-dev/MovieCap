package com.example.moviecap.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviecap.dao.MovieDao
import com.example.moviecap.database.SavedMovieRoomDatabase
import com.example.moviecap.model.MovieDB
import com.example.moviecap.model.MovieTrailer
import com.example.moviecap.model.SavedMovie
import kotlinx.coroutines.withTimeout

class SavedMovieRepository(context: Context) {

    private var movieDao: MovieDao

    private val _savedMovies: MutableLiveData<List<MovieDB>> = MutableLiveData()
    val savedMovie: LiveData<List<MovieDB>>
        get() = _savedMovies

    init {
        val savedMovieRoomDatabase = SavedMovieRoomDatabase.getDatabase(context)
        movieDao = savedMovieRoomDatabase!!.movieDao()
    }

    fun getRatedMovies(): LiveData<List<SavedMovie>> {
        return movieDao.getRatedMovies() ?: MutableLiveData(emptyList())
    }

    fun getWatchList() : LiveData<List<SavedMovie>> {
        return movieDao.getWatchList() ?: MutableLiveData(emptyList())
    }

    fun getOwnMovies() : LiveData<List<SavedMovie>> {
        return movieDao.getOwnMovies() ?: MutableLiveData(emptyList())
    }

    fun getAllSavedMovies(): LiveData<List<SavedMovie>> {
        return movieDao.getAllSavedMovies() ?: MutableLiveData(emptyList())
    }

    fun addMovie(movie: SavedMovie){
        movieDao.addMovieToList(movie)
    }

    fun updateMovie(movie: SavedMovie) {
        movieDao.updateMovie(movie)
    }

    suspend fun deleteMovie(movie: SavedMovie) {
        movieDao.removeFromList(movie)
    }

    fun deleteAll(){
        movieDao.deleteAll()
    }

    class SaveMovieFetchError(message: String, cause: Throwable) : Throwable(message, cause)
}