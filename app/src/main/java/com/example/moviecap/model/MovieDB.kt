package com.example.moviecap.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class ApiResponse() {

    data class Result (
        @SerializedName("results") val results : List<MovieDB>
    )

    data class TrailerResult (
            @SerializedName("id") val id : Int,
            @SerializedName("results") val results : List<MovieTrailer>
    )
}

@Parcelize
data class MovieDB (

    @SerializedName("title") var title: String,
    @SerializedName("backdrop_path") var backdrop_path: String,
    @SerializedName("overview") var overview: String,
    @SerializedName("poster_path") var poster_path: String,
    @SerializedName("vote_average") var vote_average: Double,
    @SerializedName("id") var id: Int,
    /**
     * TODO: Change release date to date with converter...
     */
    @SerializedName("release_date") var release_date: String
) : Parcelable {
    fun getPosterPath() = "https://image.tmdb.org/t/p/original/$poster_path"
    fun getBackdropPath() = "https://image.tmdb.org/t/p/original/$backdrop_path"
}

@Parcelize
data class MovieTrailer (

        @SerializedName("key") var key: String,
        @SerializedName("name") var name: String,
        @SerializedName("site") var site: String,
        @SerializedName("size") var size: Int
) : Parcelable {
    fun getYoutubeUrlLink() = "https://www.youtube.com/watch?v=$key"
}