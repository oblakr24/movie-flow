package com.rokoblak.flowtest.ui.main.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rokoblak.flowtest.R
import com.rokoblak.flowtest.databinding.ItemMovieBinding
import com.rokoblak.flowtest.ui.main.movies.MovieEntity


class MoviesAdapter(private val onFavouriteToggled: (MovieEntity) -> Unit)
    : ListAdapter<MovieEntity, MoviesAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MovieViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iconFavourite.setOnClickListener {
                adapterPosition.takeIf { it >=0 }?.let { getItem(it) }?.let {
                    onFavouriteToggled(it)
                }
            }
        }

        fun bind(item: MovieEntity) {
            binding.textTitle.text = item.title
            binding.textYear.text = item.year

            binding.iconFavourite.setDrawable(if (item.favourite) {
                R.drawable.ic_favorite_full
            } else {
                R.drawable.ic_favorite_empty
            })
        }
    }

    companion object {

        val DIFF_CALLBACK: DiffUtil.ItemCallback<MovieEntity> = object : DiffUtil.ItemCallback<MovieEntity>() {
            override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean = oldItem.imdbID == newItem.imdbID

            override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean = oldItem == newItem
        }
    }
}

