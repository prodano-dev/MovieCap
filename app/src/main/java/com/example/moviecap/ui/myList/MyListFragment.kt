package com.example.moviecap.ui.myList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecap.R
import com.example.moviecap.model.MovieDB
import com.example.moviecap.model.SavedMovie
import com.example.moviecap.ui.adapters.SavedMoviesAdapter
import com.example.moviecap.ui.home.BUNDLE_MOVIE_KEY
import com.example.moviecap.ui.home.REQ_MOVIE_KEY
import com.example.moviecap.viewModel.SelectedMovieViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_my_list.*

class MyListFragment : Fragment() {

    private val viewModel: SelectedMovieViewModel by viewModels()
    private val ratedMovies = arrayListOf<SavedMovie>()
    private val ratedMoviesAdapter = SavedMoviesAdapter(ratedMovies) { onMovieClick(it)}
    private val watchList = arrayListOf<SavedMovie>()
    private val watchListAdapter = SavedMoviesAdapter(watchList) { onMovieClick(it)}
    private val myMoviesList = arrayListOf<SavedMovie>()
    private val myMoviesAdapter = SavedMoviesAdapter(myMoviesList) { onMovieClick(it)}

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeMovies()
        initView()
    }

    private fun initView() {

        rvMyMovies.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvMyMovies.adapter = myMoviesAdapter

        rvSeenAndRated.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvSeenAndRated.adapter = ratedMoviesAdapter

        rvWatchlist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvWatchlist.adapter = watchListAdapter
    }

    private fun onMovieClick(movie: SavedMovie) {
        setFragmentResult(REQ_MOVIE_KEY, bundleOf(Pair(BUNDLE_MOVIE_KEY, movie)))
        findNavController().navigate(R.id.action_navigation_notifications_to_selectedMovie)
    }

    private fun observeMovies() {
        viewModel.ratedMovies.observe(viewLifecycleOwner, Observer {
            ratedMovies.clear()
            ratedMovies.addAll(it)
            ratedMoviesAdapter.notifyDataSetChanged()
        })

        viewModel.watchList.observe(viewLifecycleOwner, Observer {
            watchList.clear()
            watchList.addAll(it)
            watchListAdapter.notifyDataSetChanged()
        })

        viewModel.myOwnMovies.observe(viewLifecycleOwner, Observer {
            myMoviesList.clear()
            myMoviesList.addAll(it)
            myMoviesAdapter.notifyDataSetChanged()
        })
    }
}