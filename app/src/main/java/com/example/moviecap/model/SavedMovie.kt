package com.example.moviecap.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "savedMovieTable")
data class SavedMovie(

        @ColumnInfo(name ="title")
        var title: String,
        @ColumnInfo(name = "backdrop_path")
        var backdrop_path: String,
        @ColumnInfo(name ="overview")
        var overview: String,
        @ColumnInfo(name ="poster_path")
        var poster_path: String,
        @ColumnInfo(name ="vote_average")
        var vote_average: Double,
        @ColumnInfo(name ="movieId")
        var movieId: Int,
        @ColumnInfo(name= "ratings")
        var ratings: Double? = null,
        @ColumnInfo(name = "review")
        var review: String? = null,
        @ColumnInfo(name = "seen")
        var seen: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

) : Parcelable {
    fun getPosterPath() = "https://image.tmdb.org/t/p/original/$poster_path"
    fun getBackdropPath() = "https://image.tmdb.org/t/p/original/$backdrop_path"
}