package com.example.movieapp.ui.movieList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.movie.Movie
import com.example.movieapp.databinding.MovieListItemBinding

class MovieListAdapter(
    private val onItemClick: (Movie) -> Unit,
    private val onFavoriteClick: (Int, Movie) -> Unit
) :
    ListAdapter<Movie, MovieListViewHolder>(ITEM_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder =
        MovieListViewHolder(
            MovieListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val item: Movie = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
        holder.bind(movie = item, position = position, onFavoriteClick = onFavoriteClick)
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        //TODO
        return oldItem.id == newItem.id && oldItem.posterPath == newItem.posterPath
    }
}