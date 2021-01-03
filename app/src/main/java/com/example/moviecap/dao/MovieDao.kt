package com.example.moviecap.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviecap.model.SavedMovie

@Dao
interface MovieDao {

    @Query("SELECT * FROM savedMovieTable")
    fun getAllSavedMovies() : LiveData<List<SavedMovie>>

    @Query("SELECT * FROM savedMovieTable WHERE ratings Not null ")
    fun getRatedMovies() : LiveData<List<SavedMovie>>

    @Query("SELECT * FROM savedMovieTable WHERE ratings is null ")
    fun getWatchList() : LiveData<List<SavedMovie>>

    @Query("SELECT * FROM savedMovieTable WHERE movieId is 0 ")
    fun getOwnMovies() : LiveData<List<SavedMovie>>

    @Insert
    fun addMovieToList(movie: SavedMovie)

    @Update
    fun updateMovie(movie: SavedMovie)

    @Delete
    suspend fun removeFromList(movie: SavedMovie)

    @Query("DELETE FROM savedMovieTable")
    fun deleteAll()

}