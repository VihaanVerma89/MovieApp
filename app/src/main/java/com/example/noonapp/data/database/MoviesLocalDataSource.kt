package com.example.noonapp.data.database

import android.util.Log
import com.example.noonapp.data.database.daos.MoviesDao
import com.example.noonapp.data.database.daos.SearchTermDao
import com.example.noonapp.data.interfaces.MoviesDataSource
import com.example.noonapp.models.SearchedMovie
import io.reactivex.Flowable

class MoviesLocalDataSource(
    val moviesDao: MoviesDao,
    val searchTermDao: SearchTermDao
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
        val insert = searchTermDao.insert(searchTerm)
        val movies = searchedMovie.movies
        movies.forEach {
            it.searchTermId = insert
        }
        val insertMovies = moviesDao.insertMovies(movies)
        Log.d(TAG, "insertMovies: $insertMovies")
    }

//    private fun insertMovies(searchTerm: String, movieList: List<Movie>) {
//        val searchTermObj = SearchTerm(searchTerm = searchTerm)
//        val insert = searchTermDao.insert(searchTermObj)
//        searchTermObj.searchTermId = insert
//        movieList.forEach {
//            it.searchTermId = insert
//        }
//        val insertMovies = moviesDao.insertMovies(movieList)
//        Log.d(TAG, "insertMovies: $insertMovies")
//    }

}