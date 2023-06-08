package com.example.movieapp.ui.movieList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.movie.Movie
import com.example.movieapp.databinding.MovieListItemBinding

class MovieListAdapter(
    var movies: List<Movie>,
    private val onItemClick: (Movie) -> Unit,
    private val onFavoriteClick: (Movie) -> Unit
) :
    RecyclerView.Adapter<MovieListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder =
        MovieListViewHolder(
            MovieListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClick(movies[position])
        }
        holder.bind(movie = movies[position], onFavoriteClick = onFavoriteClick)
    }
}