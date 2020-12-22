package com.example.moviecap.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.moviecap.Model.MovieDB
import com.example.moviecap.R
import kotlinx.android.synthetic.main.fragment_movie.*


/**
 * A simple [Fragment] subclass.
 * Use the [SelectedMovie.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedMovie : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFragmentResult()
    }

    private fun observeFragmentResult() {
        setFragmentResultListener(REQ_MOVIE_KEY) { _, bundle ->
            bundle.getParcelable<MovieDB>(BUNDLE_MOVIE_KEY)?.let { setElements(it) }
        }
    }

    private fun setElements(movie: MovieDB) {
        tvMovieTitle.text = movie.title
        tvReleaseDate.text = movie.release_date
        tvMovieOverview.text = movie.overview
        tvRate.text = movie.vote_average.toString()
        context?.let { Glide.with(it).load(movie.getBackdropPath()).into(ivMovieBack) }
        context?.let { Glide.with(it).load(movie.getPosterPath()).into(ivMoviePoster) }

    }
}