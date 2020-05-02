package com.example.noonapp.data.interfaces

import com.example.noonapp.models.Movie
import io.reactivex.Flowable

interface MoviesDataSource {
    fun getMovies(searchTerm: String): Flowable<List<Movie>>
    fun insertMovies(movieList: List<Movie>)
}