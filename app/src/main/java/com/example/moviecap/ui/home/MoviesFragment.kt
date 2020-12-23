package com.example.moviecap.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecap.model.MovieDB
import com.example.moviecap.R
import com.example.moviecap.viewModel.MoveDBViewModel
import kotlinx.android.synthetic.main.fragment_home.*

const val BUNDLE_MOVIE_KEY = "bundle_movie_key"
const val REQ_MOVIE_KEY = "req_movie_key"

class MoviesFragment : Fragment() {

    private val upcomingMovies = arrayListOf<MovieDB>()
    private val upcomingMoviesAdapter = MovieAdapter(upcomingMovies) { onMovieClick(it)}
    private val topRatedMovies = arrayListOf<MovieDB>()
    private val topRatedMoviesAdapter = MovieAdapter(topRatedMovies) { onMovieClick(it)}
    private val nowPlayingMovies = arrayListOf<MovieDB>()
    private val nowPlayingMoviesAdapter = MovieAdapter(nowPlayingMovies) { onMovieClick(it)}
    private val viewModel: MoveDBViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        observeMovies()
        viewModel.getUpcomingMovies()
        viewModel.getTopRatedMovies()
        viewModel.getNowPlayingMovies()
    }

    private fun initView() {

        rvUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvUpcoming.adapter = upcomingMoviesAdapter
        rvNowPlaying.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvNowPlaying.adapter = nowPlayingMoviesAdapter
        rvTopRated.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTopRated.adapter = topRatedMoviesAdapter
    }

    private fun onMovieClick(movie: MovieDB) {
        setFragmentResult(REQ_MOVIE_KEY, bundleOf(Pair(BUNDLE_MOVIE_KEY, movie)))
        findNavController().navigate(R.id.action_navigation_home_to_selectedMovie)
    }

    private fun observeMovies() {
        viewModel.upcomingMovies.observe(viewLifecycleOwner, Observer {
            upcomingMovies.clear()
            upcomingMovies.addAll(it)
            upcomingMoviesAdapter.notifyDataSetChanged()
        })
        viewModel.topRatedMovies.observe(viewLifecycleOwner, Observer {
            topRatedMovies.clear()
            topRatedMovies.addAll(it)
            topRatedMoviesAdapter.notifyDataSetChanged()
        })
        viewModel.nowPlayingMovies.observe(viewLifecycleOwner, Observer {
            nowPlayingMovies.clear()
            nowPlayingMovies.addAll(it)
            nowPlayingMoviesAdapter.notifyDataSetChanged()
        })
    }
}