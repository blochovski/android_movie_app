package com.example.movieapp.ui.movieList

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.load
import com.example.domain.model.movie.Movie
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieListItemBinding
import com.pibi.movieApp.network.ApiConstants.IMAGES_BASE_URL

class MovieListViewHolder(private val binding: MovieListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie, position: Int, onFavoriteClick: (Int, Movie) -> Unit) = binding.apply {
        isFavouriteInList.setOnClickListener {
            onFavoriteClick(position, movie)
        }
        setIsFavouriteDrawable(movie)
        with(poster) {
            dispose()
            load("${IMAGES_BASE_URL}${movie.posterPath}") { crossfade(true) }
        }
        movieTitle.text = movie.title
        releaseDate.text = movie.releaseDate
        voteAverage.text = movie.voteAverage.toString()
    }

    private fun setIsFavouriteDrawable(movie: Movie) = binding.isFavouriteInList.setImageDrawable(
        getDrawable(
            binding.root.context,
            if (movie.isFavorite) R.drawable.favorite_black_24dp else R.drawable.favorite_border_black_24dp
        )
    )
}