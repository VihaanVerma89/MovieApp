package com.example.noonapp.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.noonapp.R
import com.example.noonapp.databinding.ItemMovieBinding
import com.example.noonapp.models.Search

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

    fun bind(search: Search) {

        binding.movieNameTv.text = search.title
        binding.movieYearTv.text = search.year

        Glide.with(binding.root.context)
            .load(search.poster)
            .into(binding.movieIv)
    }


}