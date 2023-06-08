package com.example.movieapp.ui.movieList

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.movie.Movie
import coil.dispose
import coil.load
import com.example.data.network.ApiConstants
import com.example.movieapp.databinding.MovieListItemBinding

class MovieListViewHolder(private val binding: MovieListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie) = binding.apply {
        with(movie) {
            with(poster) {
                dispose()
                load("${ApiConstants.IMAGES_BASE_URL}$posterPath") { crossfade(true) }
            }
            movieTitle.text = title
        }
    }
}