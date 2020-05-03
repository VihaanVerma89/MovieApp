package com.example.noonapp.models

import androidx.room.Embedded
import androidx.room.Relation

data class SearchedMovie(

    @Embedded val searchTerm: SearchTerm,
    @Relation(
        parentColumn = "searchTermId",
        entityColumn = "imdbID"
    )
    val movies: List<Movie>
)