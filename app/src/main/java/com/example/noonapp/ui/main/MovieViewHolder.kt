package com.example.noonapp.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.noonapp.R
import com.example.noonapp.databinding.ItemMovieBinding
import com.example.noonapp.models.Movie

class MovieViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val layout = R.layout.item_movie
        fun create(
            context: Context, inflater: LayoutInflater,
            viewGroup: ViewGroup
        ): MovieViewHolder {
            val binding = DataBindingUtil.inflate<ItemMovieBinding>(
                inflater, layout, viewGroup, false
            )
            return MovieViewHolder(
                binding
            )
        }
    }

    fun bind(movie: Movie) {

        binding.movieNameTv.text = movie.title
        binding.movieYearTv.text = movie.year
        binding.typeNameTv.text = movie.type

        Glide.with(binding.root.context)
            .load(movie.poster)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.movieIv)
    }


}