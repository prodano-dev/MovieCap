package com.example.moviecap.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecap.R
import com.example.moviecap.model.MovieDB
import com.example.moviecap.model.SavedMovie
import kotlinx.android.synthetic.main.item_movies.view.*
import kotlinx.android.synthetic.main.item_rated_movie.view.*
import kotlinx.android.synthetic.main.item_searched_movies.view.*

class SavedMoviesAdapter(private val movies: List<SavedMovie>, private val onMovieClick: (SavedMovie) -> Unit) : RecyclerView.Adapter<SavedMoviesAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {onMovieClick(movies[adapterPosition]) }
        }

        fun bind(movie: SavedMovie) {
            itemView.tvSavedMovie.text = movie.title
            val rate = if (movie.ratings == null) {
                itemView.ratings.isVisible = false
            } else movie.ratings


            itemView.tvRate.text = rate.toString()
            if (movie.movieId == 0) {
                itemView.ivSavedMovie.setImageURI(movie.poster_path.toUri())
            } else {
                Glide.with(context).load(movie.getPosterPath()).into(itemView.ivSavedMovie)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedMoviesAdapter.ViewHolder {
        context = parent.context

        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_rated_movie, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }
}