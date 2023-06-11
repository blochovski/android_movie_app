package com.example.movieapp.ui.movieDetails

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.dispose
import coil.load
import com.example.domain.model.movie.Movie
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import com.pibi.movieApp.network.ApiConstants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val binding: FragmentMovieDetailsBinding by lazy {
        FragmentMovieDetailsBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: MovieDetailsFragmentViewModel by viewModels()
    private val navController: NavController by lazy { findNavController() }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeObservables()
        initToolbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = binding.root

    private fun initBinding(movie: Movie) = binding.apply {
        with(detailsPoster) {
            dispose()
            load("${ApiConstants.IMAGES_BASE_URL}${movie.posterPath}") { crossfade(true) }
        }
        setIsFavouriteDrawable(movie)
        isFavoriteButton.setOnClickListener {
            movie.isFavorite = !movie.isFavorite
            viewModel.updateMovie(movie)
            setIsFavouriteDrawable(movie)
        }
        detailsMovieTitle.text = movie.title
        detailsReleaseDate.text = movie.releaseDate
        detailsVoteAverage.text = movie.voteAverage.toString()
        detailsMovieOverview.text = movie.overview
    }

    private fun initToolbar() = binding.detailsToolbar.apply {
        navigationIcon =
            getDrawable(
                requireContext(),
                R.drawable.back
            )
        setNavigationOnClickListener {
            navigateBackToMovieList()
        }
        background = ColorDrawable(context.getColor(R.color.card_background))
    }

    private fun observeObservables() {
        viewModel.movie.observe(this) {
            initBinding(it)
        }
    }

    private fun setIsFavouriteDrawable(movie: Movie) = binding.isFavoriteButton.setImageDrawable(
        getDrawable(
            requireContext(),
            if (movie.isFavorite) R.drawable.favorite_black_24dp else R.drawable.favorite_border_black_24dp
        )
    )

    private fun navigateBackToMovieList() = navController.popBackStack()
}