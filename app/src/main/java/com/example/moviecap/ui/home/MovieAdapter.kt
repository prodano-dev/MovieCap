package com.example.moviecap.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviecap.API.MovieDBApi
import com.example.moviecap.Model.MovieDB
import com.example.moviecap.R
import kotlinx.android.synthetic.main.item_movies.view.*

class MovieAdapter(private val movies: List<MovieDB>, private val onMovieClick: (MovieDB) -> Unit) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {onMovieClick(movies[adapterPosition]) }
        }

        fun bind(movie: MovieDB) {
            itemView.tvMovieName.text = movie.title
            Glide.with(context).load(movie.getPosterPath()).into(itemView.ivPoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_movies, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position])
    }
}