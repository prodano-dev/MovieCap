package com.example.moviecap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviecap.dao.MovieDao
import com.example.moviecap.model.SavedMovie

@Database(entities = [SavedMovie::class], version = 1, exportSchema = false)
abstract class SavedMovieRoomDatabase : RoomDatabase() {

    abstract fun movieDao() : MovieDao

    companion object {
        private const val DATABASE_NAME = "MOVIE_DATABASE"

        @Volatile
        private var movieRoomDatabaseInstance: SavedMovieRoomDatabase? = null

        fun getDatabase(context: Context) : SavedMovieRoomDatabase? {
            if (movieRoomDatabaseInstance == null) {
                synchronized(SavedMovieRoomDatabase::class.java) {
                    if (movieRoomDatabaseInstance == null) {
                        movieRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            SavedMovieRoomDatabase::class.java,
                            DATABASE_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }

            return movieRoomDatabaseInstance
        }
    }
}