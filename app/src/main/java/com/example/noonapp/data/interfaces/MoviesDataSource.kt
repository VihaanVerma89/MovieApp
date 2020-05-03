package com.example.noonapp.data.interfaces

import com.example.noonapp.models.SearchedMovie
import io.reactivex.Flowable

interface MoviesDataSource {
    fun getMovies(searchTerm: String): Flowable<SearchedMovie>?
    fun insertMovies(searchedMovie: SearchedMovie)
}