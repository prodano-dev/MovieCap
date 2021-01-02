package com.example.moviecap.ui.home

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.AlertDialog
import android.content.DialogInterface
import android.icu.text.CaseMap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.media2.exoplayer.external.util.Util
import androidx.navigation.fragment.findNavController
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.bumptech.glide.Glide
import com.example.moviecap.R
import com.example.moviecap.model.MovieDB
import com.example.moviecap.model.MovieTrailer
import com.example.moviecap.model.SavedMovie
import com.example.moviecap.viewModel.MoveDBViewModel
import com.example.moviecap.viewModel.SelectedMovieViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.fragment_movie.*


/**
 * A simple [Fragment] subclass.
 * Use the [SelectedMovie.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedMovie : Fragment() {

    private val saveViewModel: SelectedMovieViewModel by viewModels()
    private val movieDBviewModel: MoveDBViewModel by activityViewModels()
    private val movieTrailers = arrayListOf<MovieTrailer>()
    private var playerView = trailerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private var playWhenReady = true;
    private var currentWindow = 0;
    private var playbackPosition = 0;

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
        playerView = trailerView
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

    }

    /**
     * ExoPlayer
     */

    private fun initExoPlayer() {

        simpleExoPlayer = SimpleExoPlayer.Builder(requireActivity()).build()
        playerView.player = simpleExoPlayer
        simpleExoPlayer.playWhenReady = playWhenReady
        simpleExoPlayer.seekTo(currentWindow, playbackPosition.toLong())
    }

    private fun extractYoutubeUrl() {
        for (trailer in movieTrailers) {
            @SuppressLint("StaticFieldLeak") val mExtractor: YouTubeExtractor = object : YouTubeExtractor(requireActivity()) {
                override fun onExtractionComplete(sparseArray: SparseArray<YtFile?>?, videoMeta: VideoMeta?) {
                    if (sparseArray != null) {
                        var mediaItem = MediaItem.fromUri(Uri.parse(sparseArray.get(18)!!.url.toString()))
                        Log.e("link", mediaItem.toString())
                        simpleExoPlayer.addMediaItem(mediaItem)

                    }
                }
            }
            mExtractor.extract(trailer.getYoutubeUrlLink(), true, true)
        }
    }

    override fun onDetach() {
        super.onDetach()
        simpleExoPlayer.clearMediaItems()
    }

    override fun onStart() {
        super.onStart()
        if ( Util.SDK_INT >= 24) {
            initExoPlayer();
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || simpleExoPlayer == null) {
            initExoPlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (simpleExoPlayer != null) {
            playWhenReady = simpleExoPlayer.playWhenReady
            playbackPosition = simpleExoPlayer.currentPosition.toInt();
            currentWindow = simpleExoPlayer.currentWindowIndex
            simpleExoPlayer.release();
        }
    }

    /**
     * Actions
     */

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
                saveViewModel.changeRatings(stars.rating.toDouble())
                saveViewModel.setDatabaseIdForMovie()
                saveViewModel.updateMovie(saveViewModel.selectedMovie!!)
            } else {
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

    /**
     * Obervers
     */

    private fun observeFragmentResult() {
        setFragmentResultListener(REQ_MOVIE_KEY) { _, bundle ->
            bundle.getParcelable<MovieDB>(BUNDLE_MOVIE_KEY)?.let {
                (activity as AppCompatActivity).supportActionBar?.title = it.title
                movieDBviewModel.getMovieTrailer(it.id)
                setElements(it) }
        }
        observeTrailers()
    }

    private fun observeTrailers() {
        movieDBviewModel.movieTrailers.observe(viewLifecycleOwner, Observer {
            movieTrailers.clear()
            movieTrailers.addAll(it)
            extractYoutubeUrl()
        })

    }

    private fun observeMovies() {
        saveViewModel.savedMovies.observe(viewLifecycleOwner, Observer {
            saveViewModel.savedMoviesArray.clear()
            saveViewModel.savedMoviesArray.addAll(it)
            updateUI()
        })
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