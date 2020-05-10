package com.example.noonapp.data

import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter

interface MoviesRepository {

    fun getMovies(searchTerm: String): Flowable<Any>

    fun getMoviesFromLocalDataSource(
        emitter: FlowableEmitter<Any>,
        searchTerm: String
    )

    fun getMoviesFromRemoteDataSource(searchTerm: String): Flowable<SearchedMovie>
    fun getAndInsertMoviesFromRemoteDataSource(
        emitter: FlowableEmitter<Any>,
        searchTerm: String
    )
    fun insertMovies(searchedMovie: SearchedMovie)
}