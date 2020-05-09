package com.example.noonapp.data.models

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.example.noonapp.data.DataSource

data class SearchedMovie(

    @Embedded val searchTerm: SearchTerm,
    @Relation(
        parentColumn = "searchTerm",
        entityColumn = "searchTerm"
    )
    val movies: List<Movie>
) {
    @Transient
    @Ignore
    var source: String = ""

    fun isFromNetwork(): Boolean {
        var network = false
        if (source.equals(DataSource.NETWORK, true)) {
            network = true
        }
        return network
    }

    fun isFromDatabase(): Boolean {
        var database = false
        if (source.equals(DataSource.DATABASE, true)) {
            database = true
        }
        return database
    }

    override fun toString(): String {
        return "SearchedMovie(source='$source',searchTerm=$searchTerm, movies=$movies)"
    }


}
