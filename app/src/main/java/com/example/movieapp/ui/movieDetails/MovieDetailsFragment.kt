package com.example.movieapp.ui.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.dispose
import coil.load
import com.example.data.network.model.ApiConstants
import com.example.domain.model.movie.Movie
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val binding: FragmentMovieDetailsBinding by lazy {
        FragmentMovieDetailsBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: MovieDetailsFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeObservables()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    private fun initBinding(movie: Movie) = binding.apply {
        detailsMovieTitle.text = movie.title
        with(detailsPoster) {
            dispose()
            load("${ApiConstants.IMAGES_BASE_URL}${movie.posterPath}") { crossfade(true) }
        }
    }
    private fun observeObservables() {
        viewModel.movie.observe(this) {
            initBinding(it)
        }
    }
}