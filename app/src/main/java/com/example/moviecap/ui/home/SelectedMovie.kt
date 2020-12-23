package com.example.moviecap.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.moviecap.model.MovieDB
import com.example.moviecap.R
import com.example.moviecap.model.SavedMovie
import com.example.moviecap.viewModel.SelectedMovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*


/**
 * A simple [Fragment] subclass.
 * Use the [SelectedMovie.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedMovie : Fragment() {

    private val saveViewModel: SelectedMovieViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addToWatchListButton.setOnClickListener {

            didTappedAddToWatchlist()
        }

        rateButton.setOnClickListener {
            didTappedRateButton()
        }

        observeFragmentResult()
    }

    private fun  didTappedRateButton() {

        saveViewModel.savedMovies.observe(viewLifecycleOwner, Observer {
            Log.e("savedmovie title", it[0].title)
        })
    }


    private fun didTappedAddToWatchlist() {

        saveViewModel.addMovieToList(saveViewModel.selectedMovie!!)

    }

    private fun observeFragmentResult() {
        setFragmentResultListener(REQ_MOVIE_KEY) { _, bundle ->
            bundle.getParcelable<MovieDB>(BUNDLE_MOVIE_KEY)?.let { setElements(it) }
        }
    }

    private fun setElements(movie: MovieDB) {

        var selectedMovie = SavedMovie(
            title = movie.title,
            backdrop_path = movie.backdrop_path,
            overview = movie.overview,
            poster_path = movie.poster_path,
            vote_average = movie.vote_average,
            movieId = movie.id
        )

        saveViewModel.selectedMovie = selectedMovie

        tvMovieTitle.text = movie.title
        tvReleaseDate.text = movie.release_date
        tvMovieOverview.text = movie.overview
        context?.let { Glide.with(it).load(movie.getBackdropPath()).into(ivMovieBack) }
        context?.let { Glide.with(it).load(movie.getPosterPath()).into(ivMoviePoster) }

    }
}