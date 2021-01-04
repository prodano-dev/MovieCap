package com.example.moviecap.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.moviecap.R
import com.example.moviecap.model.SavedMovie
import com.example.moviecap.viewModel.SelectedMovieViewModel
import kotlinx.android.synthetic.main.fragment_add_movie.*


class AddMovieDialog: DialogFragment() {

    private val saveViewModel: SelectedMovieViewModel by viewModels()
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_movie, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveButton.setOnClickListener {
            saveMovie()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        movieImageView.setOnClickListener {
            openCamera()
        }
    }

    private fun saveMovie() {
        val movie = SavedMovie(
                title = movieNameET.text.toString(),
                backdrop_path = "",
                overview = "Not available",
                poster_path = image_uri.toString(),
                vote_average = 0.0,
                movieId = 0,
                seen = true,
                ratings = ratingBar.rating.toDouble(),
                review = movieAboutET.text.toString()
        )

        saveViewModel.addMovieToList(movie)
        dismiss()
    }

    private fun openCamera() {
        val resolver = requireActivity().contentResolver
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            movieImageView.setImageURI(image_uri)
        }
    }
}