package com.example.moviecap.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.moviecap.model.SavedMovie

@Dao
interface MovieDao {

    @Query("SELECT * FROM savedMovieTable")
    fun getAllSavedMovies() : LiveData<List<SavedMovie>>

    @Insert
    fun addMovieToList(movie: SavedMovie)

    @Update
    fun updateMovie(movie: SavedMovie)

}