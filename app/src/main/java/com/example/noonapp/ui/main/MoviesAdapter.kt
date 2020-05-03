package com.example.noonapp.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noonapp.models.Movie

class MoviesAdapter(val context: Context) :
    ListAdapter<Any, RecyclerView.ViewHolder>(MoviesDiffCallback(context)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var viewHolder: RecyclerView.ViewHolder? = null
        when (viewType) {
            MovieViewHolder.layout -> viewHolder = MovieViewHolder.create(context, inflater, parent)
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MovieViewHolder -> holder.bind(item as Movie)
        }
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        var itemViewType: Int = MovieViewHolder.layout
        val item = getItem(position)

        when (item) {
            is Movie -> itemViewType = MovieViewHolder.layout
        }
        return itemViewType
    }

    public override fun getItem(position: Int): Any {
        return super.getItem(position)
    }

}
