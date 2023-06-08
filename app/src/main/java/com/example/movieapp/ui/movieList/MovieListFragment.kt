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
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.movie.Movie
import com.example.exceptions.NoMoreMoviesException
import com.example.movieapp.databinding.FragmentMovieListBinding
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Loaded
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Loading
import dagger.hilt.android.AndroidEntryPoint
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Error as StateError

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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun observeViewStateUpdates(adapter: MovieListAdapter) {
        viewModel.state.observe(viewLifecycleOwner) {
            updateScreenState(it, adapter)
        }
    }

    private fun setupUI() {
        val adapter = newAdapter()
        initMovieList(adapter)
        observeViewStateUpdates(adapter)
    }

    private fun initMovieList(movieListAdapter: MovieListAdapter) = binding.movieList.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = movieListAdapter
        setHasFixedSize(true)
        addOnScrollListener(
            newInfiniteScrollListener(layoutManager as LinearLayoutManager)
        )
    }

    private fun newInfiniteScrollListener(
        layoutManager: LinearLayoutManager
    ): RecyclerView.OnScrollListener {
        return object : InfiniteScrollListener(layoutManager, PAGE_SIZE) {
            override fun loadMoreItems() = onMoviesEvent()
            override fun isLoading(): Boolean = viewModel.isLoadingMoreMovies
            override fun isLastPage(): Boolean = viewModel.isLastPage
        }
    }

    private fun newAdapter(): MovieListAdapter =
        MovieListAdapter(::navigateToMovieDetails, ::onFavoriteClick)


    private fun navigateToMovieDetails(movie: Movie) = navController.navigate(
        MovieListFragmentDirections.actionMovieListFragmentToMovieDetailsFragment(
            movie = movie
        )
    )

    private fun onFavoriteClick(index: Int, movie: Movie) {
        movie.isFavorite = !movie.isFavorite
        binding.movieList.adapter?.apply { notifyItemChanged(index) }
        viewModel.updateMovie(movie)
    }

    fun onMoviesEvent() {
        viewModel.loadNextMoviesPage()
    }

    private fun onError(error: Throwable) {
        when (error) {
            is NoMoreMoviesException -> Unit
        }
    }

    private fun updateScreenState(state: MovieListFragmentUiState, adapter: MovieListAdapter) {
        when (state) {
            Loading -> Unit // TODO binding.progressBar.isVisible = true
            is Loaded -> {
                // TODO binding.progressBar.isVisible = false
                adapter.submitList(state.movies)
            }

            is StateError -> onError(state.error)
        }
    }

    companion object {
        val PAGE_SIZE = 20
    }
}