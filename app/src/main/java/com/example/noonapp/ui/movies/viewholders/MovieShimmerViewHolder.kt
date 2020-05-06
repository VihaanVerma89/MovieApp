package com.example.noonapp.ui.movies.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noonapp.R
import com.example.noonapp.data.models.MovieShimmer
import com.example.noonapp.databinding.ItemMovieShimmerBinding

class MovieShimmerViewHolder(val binding: ItemMovieShimmerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        const val layout = R.layout.item_movie_shimmer
        fun create(
            context: Context, inflater: LayoutInflater,
            viewGroup: ViewGroup
        ): MovieShimmerViewHolder {
            val binding = DataBindingUtil.inflate<ItemMovieShimmerBinding>(
                inflater,
                layout, viewGroup, false
            )
            return MovieShimmerViewHolder(
                binding
            )
        }
    }

    fun bind(movie: MovieShimmer) {
        binding.shimmerFl.startShimmer()
    }


}