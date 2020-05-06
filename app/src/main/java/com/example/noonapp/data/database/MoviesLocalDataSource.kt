package com.example.noonapp.data.database

import android.util.Log
import com.example.noonapp.data.database.daos.MoviesDao
import com.example.noonapp.data.database.daos.SearchTermDao
import com.example.noonapp.data.interfaces.MoviesDataSource
import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.Flowable
import javax.inject.Inject

class MoviesLocalDataSource @Inject constructor(
    private val moviesDao: MoviesDao,
    private val searchTermDao: SearchTermDao
) : MoviesDataSource {


    companion object {
        val TAG = MoviesLocalDataSource::class.java.name
    }

    override fun getMovies(searchTerm: String): Flowable<SearchedMovie> {
        val searchedMovie = searchTermDao.getSearchedMovie(searchTerm)
        return searchedMovie
    }

    override fun insertMovies(searchedMovie: SearchedMovie) {
        val searchTerm = searchedMovie.searchTerm
        searchTermDao.insert(searchTerm)
        val movies = searchedMovie.movies
        movies.forEach {
            it.searchTerm = searchTerm.searchTerm
        }
        val insertMovies = moviesDao.insertMovies(movies)
        Log.d(TAG, "insertMovies: $insertMovies")
    }
}