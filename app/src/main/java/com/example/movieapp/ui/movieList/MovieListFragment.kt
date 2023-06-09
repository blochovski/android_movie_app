package com.example.movieapp.ui.movieList

import android.annotation.SuppressLint
import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
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
import com.example.movieapp.ui.movieList.MovieListFragmentUiState.Searching
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        val adapter = newAdapter()
        initMovieList(adapter)
        initSearchView()
        observeViewStateUpdates(adapter)
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

    fun onMoviesEvent() {
        when (viewModel.state.value) {
            is Loaded -> viewModel.loadNextMoviesPage(isLoadingMoreMovies = true)
            is Searching -> viewModel.searchRemotelyNextMoviesPage()
            else -> Unit
        }
    }

    private fun initSearchView() = binding.searchView.apply {

        setQueryTextListener()
        setSuggestionAdapter()
        setSuggestionListener()
    }

    private fun SearchView.setQueryTextListener() {
        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.updateQuery(query.orEmpty())
                clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateQuery(newText.orEmpty())

                return true
            }
        })
    }


    private fun SearchView.setSuggestionAdapter() {
        suggestionsAdapter = SimpleCursorAdapter(
            context,
            android.R.layout.simple_list_item_1,
            /*cursor*/ null,
            /*from*/ arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
            /*to*/ intArrayOf(android.R.id.text1),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
    }

    private fun SearchView.setSuggestionListener() {
        setOnSuggestionListener(
            object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    onSuggestion(position)
                    return true
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    onSuggestion(position)
                    return true
                }
            })
    }

    @SuppressLint("Range")
    private fun onSuggestion(position: Int) {
        val cursor = (binding.searchView.suggestionsAdapter.getItem(position) as Cursor)
        val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
        binding.searchView.setQuery(selection, false)
        viewModel.searchSuggestionRemotely(selection)
    }

    private fun observeViewStateUpdates(adapter: MovieListAdapter) {
        viewModel.state.observe(viewLifecycleOwner) {
            updateUi(it, adapter)
        }
    }

    private fun updateUi(state: MovieListFragmentUiState, adapter: MovieListAdapter) {
        when (state) {
            Loading ->
                updateUiWhenLoading()

            is Searching ->
                updateUiWhenSearching(adapter, state)

            is Loaded ->
                updateUiWhenLoaded(adapter, state)

            is StateError ->
                updateUiWhenError(state)
        }
    }

    private fun updateUiWhenLoading() {
        binding.progressBar.isVisible = true
    }

    private fun updateUiWhenSearching(
        adapter: MovieListAdapter,
        state: Searching
    ) {
        binding.progressBar.isVisible = false
        adapter.submitList(state.movies)
        binding.searchView.apply {
            val cursor =
                MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))

            addRowsToCursor(state.movies, cursor)
            suggestionsAdapter.changeCursor(cursor)
        }
    }

    private fun SearchView.addRowsToCursor(
        movies: List<Movie>,
        cursor: MatrixCursor
    ) {
        query?.let {
            movies
                .map { it.title }
                .forEachIndexed { index, suggestion ->
                    if (suggestion.contains(query, true))
                        cursor.addRow(arrayOf(index, suggestion))
                }
        }
    }

    private fun updateUiWhenLoaded(
        adapter: MovieListAdapter,
        state: Loaded
    ) {
        binding.progressBar.isVisible = false
        adapter.submitList(state.movies)
    }

    private fun updateUiWhenError(state: MovieListFragmentUiState.Error) {
        binding.progressBar.isVisible = false
        onError(state.error)
    }

    private fun onError(error: Throwable) {
        when (error) {
            is NoMoreMoviesException -> Unit
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}