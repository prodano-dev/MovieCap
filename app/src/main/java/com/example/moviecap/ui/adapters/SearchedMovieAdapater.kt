package com.example.moviecap.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecap.R
import com.example.moviecap.model.MovieDB
import kotlinx.android.synthetic.main.item_movies.view.*
import kotlinx.android.synthetic.main.item_searched_movies.view.*

class SearchedMovieAdapater(private val movies: List<MovieDB>, private val onMovieClick: (MovieDB) -> Unit) : RecyclerView.Adapter<SearchedMovieAdapater.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {onMovieClick(movies[adapterPosition]) }
        }

        fun bind(movie: MovieDB) {
            itemView.tvSearchedMovie.text = movie.title
            Glide.with(context).load(movie.getPosterPath()).into(itemView.ivSearchedMovie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchedMovieAdapater.ViewHolder {
        context = parent.context

        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_searched_movies, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }
}