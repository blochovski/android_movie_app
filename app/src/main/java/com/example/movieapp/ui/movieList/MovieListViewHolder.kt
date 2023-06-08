package com.example.movieapp.ui.movieList

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import coil.dispose
import coil.load
import com.example.data.network.ApiConstants
import com.example.domain.model.movie.Movie
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieListItemBinding

class MovieListViewHolder(private val binding: MovieListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(movie: Movie, onFavoriteClick: (Movie) -> Unit) = binding.apply {
        with(movie) {
            setIsFavouriteDrawable(movie)

            isFavouriteInList.setOnClickListener {
                movie.isFavorite = !movie.isFavorite
                onFavoriteClick(movie)
                setIsFavouriteDrawable(movie)
            }

            with(poster) {
                dispose()
                load("${ApiConstants.IMAGES_BASE_URL}$posterPath") { crossfade(true) }
            }
            movieTitle.text = title
        }
    }

    private fun setIsFavouriteDrawable(movie: Movie) = binding.isFavouriteInList.setImageDrawable(
        getDrawable(
            binding.root.context,
            if (movie.isFavorite) R.drawable.favorite_black_24dp else R.drawable.favorite_border_black_24dp
        )
    )
}