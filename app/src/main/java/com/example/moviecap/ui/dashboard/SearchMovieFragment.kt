package com.example.moviecap.ui.dashboard

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.camera.core.ImageCapture
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviecap.R
import com.example.moviecap.model.MovieDB
import com.example.moviecap.ui.AddMovieDialog
import com.example.moviecap.ui.home.BUNDLE_MOVIE_KEY
import com.example.moviecap.ui.home.MovieAdapter
import com.example.moviecap.ui.home.REQ_MOVIE_KEY
import com.example.moviecap.viewModel.MoveDBViewModel
import kotlinx.android.synthetic.main.fragment_search_movie.*
import java.io.File
import java.util.concurrent.ExecutorService

class SearchMovieFragment : Fragment() {


    private val viewModel: MoveDBViewModel by activityViewModels()
    private val searchedMovies = arrayListOf<MovieDB>()
    private val searchedMoviesAdapter = MovieAdapter(searchedMovies) { onMovieClick(it)}

    private var imageCapture: ImageCapture? = null
    private lateinit var outPutDirectory: File
    private lateinit var cameraExecutor: ExecutorService


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
        filterButton.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        val fm = fragmentManager
        val editNameDialogFragment = AddMovieDialog()
        editNameDialogFragment.setTargetFragment(this@SearchMovieFragment, 300)
        editNameDialogFragment.show(fm!!, "fragment_edit_name")
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Add movie")
//        imageCapture = ImageCapture.Builder().build()
//
//        val dialogLayout = layoutInflater.inflate(R.layout.fragment_add_movie, null)
//        builder.setView(dialogLayout)
//        builder.setPositiveButton("ADD") { _: DialogInterface, _: Int ->
//
//
//        }
//        builder.setNegativeButton("CANCEL") {dialogInterface, which ->
//            Toast.makeText(context, "NEE", Toast.LENGTH_LONG).show()
//        }
//        builder.show()

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

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}