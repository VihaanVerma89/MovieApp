package com.example.noonapp.data.network

import com.example.noonapp.BuildConfig
import com.example.noonapp.data.interfaces.MoviesDataSource
import com.example.noonapp.models.Movie
import io.reactivex.Flowable

class MoviesRemoteDataSource(val moviesService: MoviesService) : MoviesDataSource {

    private val apiKey = BuildConfig.CONSUMER_KEY
    override fun getMovies(searchTerm: String): Flowable<List<Movie>> {
//        val moviesResponse = moviesService.getMoviesResponse(searchTerm, apiKey).blockingGet()
        return moviesService.getMoviesResponse(searchTerm, apiKey)
            .map {
                it.movies
            }
            .toFlowable()
//        return Flowable.fromIterable(moviesResponse.movies).toList().toFlowable()
    }

    override fun insertMovies(movieList: List<Movie>) {

    }
}