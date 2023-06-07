package com.example.movieapp.ui.movieList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movieapp.databinding.FragmentMovieListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val binding: FragmentMovieListBinding by lazy {
        FragmentMovieListBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: MovieListFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeObservables()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    private fun observeObservables() {
        viewModel.movies.observe(this){
            binding.tempText.text = it.toString()
        }
    }
}