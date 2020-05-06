package com.example.noonapp.ui.movies

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noonapp.data.models.Movie
import com.example.noonapp.data.models.MovieShimmer
import com.example.noonapp.ui.movies.viewholders.MovieShimmerViewHolder
import com.example.noonapp.ui.movies.viewholders.MovieViewHolder

class MoviesAdapter(val context: Context) :
    ListAdapter<Any, RecyclerView.ViewHolder>(MoviesDiffCallback(context)) {
    companion object {
        val TAG = MoviesAdapter::class.java.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var viewHolder: RecyclerView.ViewHolder? = null
        when (viewType) {
            MovieViewHolder.layout -> viewHolder = MovieViewHolder.create(context, inflater, parent)
            MovieShimmerViewHolder.layout -> viewHolder =
                MovieShimmerViewHolder.create(context, inflater, parent)
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        Log.d(TAG, "onBindViewHolder: $item")
        when (holder) {
            is MovieViewHolder -> holder.bind(item as Movie)
            is MovieShimmerViewHolder -> holder.bind(item as MovieShimmer)
        }
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        var itemViewType: Int = MovieViewHolder.layout
        val item = getItem(position)

        when (item) {
            is Movie -> itemViewType = MovieViewHolder.layout
            is MovieShimmer -> itemViewType = MovieShimmerViewHolder.layout
        }
        return itemViewType
    }

    public override fun getItem(position: Int): Any {
        return super.getItem(position)
    }

}
