package com.example.moviecap.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviecap.model.MovieDB
import com.example.moviecap.R
import com.example.moviecap.viewModel.MoveDBViewModel
import com.example.moviecap.ui.home.BUNDLE_MOVIE_KEY
import com.example.moviecap.ui.home.MovieAdapter
import com.example.moviecap.ui.home.REQ_MOVIE_KEY
import kotlinx.android.synthetic.main.fragment_search_movie.*

class SearchMovieFragment : Fragment() {


    private val viewModel: MoveDBViewModel by activityViewModels()
    private val searchedMovies = arrayListOf<MovieDB>()
    private val searchedMoviesAdapter = MovieAdapter(searchedMovies) { onMovieClick(it)}

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextChange(newText: String): Boolean {

                if (newText == ""){
                    searchedMovies.clear()
                } else {
                    viewModel.searchMovies(newText)
                }

                return false
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }

        })

        initView()
        observeMovies()

    }

    private fun onMovieClick(movie: MovieDB) {
        setFragmentResult(REQ_MOVIE_KEY, bundleOf(Pair(BUNDLE_MOVIE_KEY, movie)))
        findNavController().navigate(R.id.action_navigation_search_movies_to_selectedMovie)
    }

    private fun initView() {
        rvSearchedMovies.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        rvSearchedMovies.adapter = searchedMoviesAdapter
    }

    private fun observeMovies() {
        viewModel.searchedMovies.observe(viewLifecycleOwner, Observer {
            searchedMovies.clear()
            searchedMovies.addAll(it)
            searchedMoviesAdapter.notifyDataSetChanged()
        })

    }

}