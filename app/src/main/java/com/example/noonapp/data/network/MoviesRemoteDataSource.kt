package com.example.noonapp.data.network

import com.example.noonapp.BuildConfig
import com.example.noonapp.data.DataSource
import com.example.noonapp.data.interfaces.MoviesDataSource
import com.example.noonapp.data.models.SearchTerm
import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.Flowable
import javax.inject.Inject

class MoviesRemoteDataSource @Inject constructor(val moviesService: MoviesService) :
    MoviesDataSource {

    private val apiKey = BuildConfig.CONSUMER_KEY
    override fun getMovies(searchTerm: String): Flowable<SearchedMovie> {
        return moviesService.getMoviesResponse(searchTerm, apiKey)
            .map {
                val searchTermObj = SearchTerm(searchTerm = searchTerm)
                val searchedMovie = SearchedMovie(searchTermObj, it.movies)
                searchedMovie.source = DataSource.NETWORK
                searchedMovie
            }
            .toFlowable()
    }

    override fun insertMovies(searchedMovie: SearchedMovie) {

    }

}