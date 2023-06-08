package com.example.movieapp.ui.movieList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.movie.Movie
import com.example.movieapp.databinding.FragmentMovieListBinding
import com.example.movieapp.ui.movieDetails.MovieDetailsFragment
import com.example.movieapp.ui.movieDetails.MovieDetailsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val binding: FragmentMovieListBinding by lazy {
        FragmentMovieListBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: MovieListFragmentViewModel by viewModels()
    private val navController: NavController by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMovieList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeObservables()
    }

    private fun observeObservables() {
        viewModel.movies.observe(viewLifecycleOwner) {
            binding.movieList.adapter = MovieListAdapter(it)
            navigateToMovieDetails(it.first())
        }
    }

    private fun initMovieList() = binding.movieList.apply {
        layoutManager = LinearLayoutManager(context)
    }

    private fun navigateToMovieDetails(movie: Movie) = navController.navigate(
        MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
            movie = movie
        )
    )
}