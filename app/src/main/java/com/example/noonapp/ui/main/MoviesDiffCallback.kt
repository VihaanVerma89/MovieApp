package com.example.noonapp.ui.main

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import com.example.noonapp.data.models.Movie

class MoviesDiffCallback(context: Context) : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        var same = false
        if (oldItem is Movie && newItem is Movie) {
            same = oldItem.imdbID == newItem.imdbID
        }
        return same
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        var same = false
        if (oldItem is Movie && newItem is Movie) {
            same = oldItem == newItem
        }
        return same
    }

}
