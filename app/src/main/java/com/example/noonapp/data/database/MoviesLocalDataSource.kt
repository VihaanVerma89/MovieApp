package com.example.noonapp.data.database

import android.util.Log
import com.example.noonapp.data.database.daos.MoviesDao
import com.example.noonapp.data.interfaces.MoviesDataSource
import com.example.noonapp.models.Movie
import io.reactivex.Flowable

class MoviesLocalDataSource(val moviesDao: MoviesDao) : MoviesDataSource {


    companion object {
        val TAG = MoviesLocalDataSource::class.java.name
    }

    override fun getMovies(searchTerm: String): Flowable<List<Movie>> {
        return moviesDao.getMovies()
    }

    override fun insertMovies(movieList: List<Movie>){
        val insertMovies = moviesDao.insertMovies(movieList)
        Log.d(TAG, "insertMovies: $insertMovies")
    }

}