package com.example.moviecap.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.moviecap.model.MovieDB
import com.example.moviecap.R
import com.example.moviecap.model.SavedMovie
import com.example.moviecap.viewModel.SelectedMovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_rating_dialog.*
import kotlinx.android.synthetic.main.fragment_rating_dialog.view.*


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
        observeMovies()

    }

    private fun updateUI() {

        if (saveViewModel.isInWatchList()) {
            saveViewModel.setDatabaseIdForMovie()
            addToWatchListButton.text = "Remove from list"
        } else {
            addToWatchListButton.text = "Add to watchlist"
        }

        if (saveViewModel.hasBeenRated()) {
            rateButton.text = saveViewModel.selectedMovie!!.ratings.toString()
        }

        Log.e("again", saveViewModel.selectedMovie!!.ratings.toString())
    }
    private fun observeMovies() {

        saveViewModel.savedMovies.observe(viewLifecycleOwner, Observer {
            saveViewModel.savedMoviesArray.clear()
            saveViewModel.savedMoviesArray.addAll(it)
            updateUI()
        })

    }

    private fun  didTappedRateButton() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(saveViewModel.selectedMovie!!.title)
        val dialogLayout = layoutInflater.inflate(R.layout.fragment_rating_dialog, null)
        val thougs = dialogLayout.findViewById<EditText>(R.id.txt_amount)
        val stars = dialogLayout.findViewById<RatingBar>(R.id.ratingBar)


        builder.setView(dialogLayout)

        builder.setPositiveButton("RATE") { _: DialogInterface, _: Int ->

            saveViewModel.selectedMovie!!.ratings = stars.rating.toDouble()
            saveViewModel.selectedMovie!!.review = thougs.text.toString()
            if (saveViewModel.isInWatchList()) {
                saveViewModel.changeiests(stars.rating.toDouble())
                saveViewModel.setDatabaseIdForMovie()
                saveViewModel.updateMovie(saveViewModel.selectedMovie!!)
                Log.e("true", stars.rating.toString())
            } else {
                Log.e("false", saveViewModel.selectedMovie!!.id.toString())
                saveViewModel.addMovieToList(saveViewModel.selectedMovie!!)
            }

        }
        builder.show()
    }


    private fun didTappedAddToWatchlist() {

        if (addToWatchListButton.text == "Remove from list") {
            saveViewModel.setDatabaseIdForMovie()
            saveViewModel.removieMovieFromList(saveViewModel.selectedMovie!!)
        } else {
            saveViewModel.addMovieToList(saveViewModel.selectedMovie!!)
        }

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