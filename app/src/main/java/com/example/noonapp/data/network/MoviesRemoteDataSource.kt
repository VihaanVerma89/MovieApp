package com.example.noonapp.data.network

import com.example.noonapp.BuildConfig
import com.example.noonapp.data.interfaces.MoviesDataSource
import com.example.noonapp.data.models.SearchTerm
import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.Flowable

class MoviesRemoteDataSource(val moviesService: MoviesService) : MoviesDataSource {

    private val apiKey = BuildConfig.CONSUMER_KEY
    //    override fun getMovies(searchTerm: String): Flowable<SearchedMovie> {
    override fun getMovies(searchTerm: String): Flowable<SearchedMovie> {
//        val moviesResponse = moviesService.getMoviesResponse(searchTerm, apiKey).blockingGet()
        return moviesService.getMoviesResponse(searchTerm, apiKey)
            .map {
                val searchTermObj = SearchTerm(searchTerm = searchTerm)
                val searchedMovie = SearchedMovie(searchTermObj, it.movies)
                searchedMovie
            }
            .toFlowable()
//        return Flowable.fromIterable(moviesResponse.movies).toList().toFlowable()
    }

    override fun insertMovies(searchedMovie: SearchedMovie) {

    }

//    override fun insertMovies(searchTerm: String, movieList: List<Movie>) {
//
//    }

}