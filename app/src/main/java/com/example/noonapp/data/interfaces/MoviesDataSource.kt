package com.example.noonapp.data.interfaces

import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.Flowable

interface MoviesDataSource {
//    fun getMovies(searchTerm: String): Flowable<List<SearchedMovie>>
    fun getMovies(searchTerm: String): Flowable<SearchedMovie>
//    fun getMovies(searchTerm: String): List<SearchedMovie>
    fun insertMovies(searchedMovie: SearchedMovie)
}