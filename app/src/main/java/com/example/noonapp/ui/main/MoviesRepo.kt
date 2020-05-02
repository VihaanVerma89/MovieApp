package com.example.noonapp.ui.main

import android.util.Log
import com.example.noonapp.data.database.MoviesLocalDataSource
import com.example.noonapp.data.interfaces.MoviesDataSource
import com.example.noonapp.data.network.MoviesRemoteDataSource
import com.example.noonapp.models.Movie
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MoviesRepo(
    val moviesLocalDataSource: MoviesLocalDataSource,
    val moviesRemoteDataSource: MoviesRemoteDataSource
) : MoviesDataSource {

    companion object {
        val TAG = MoviesRepo::class.java.name
    }

//    fun getMoviesResponse(searchTerm: String): Flowable<> {
//    }

    private fun getAndSaveMoviesFromRemoteDataSource(searchTerm: String) {
        getMoviesFromRemoteDataSource(searchTerm)
            .subscribeOn(Schedulers.io())
            .delay(10, TimeUnit.SECONDS)
            .map {
                moviesLocalDataSource.insertMovies(it)
                it
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "getAndSaveMoviesFromRemoteDataSource: onNext $it")
                }, {
                    Log.d(TAG, "getAndSaveMoviesFromRemoteDataSource: onError $it")
                }, {
                    Log.d(TAG, "getAndSaveMoviesFromRemoteDataSource: onComplete")
                }
            )

    }

    override fun getMovies(searchTerm: String): Flowable<List<Movie>> {
        getAndSaveMoviesFromRemoteDataSource(searchTerm)
        return getMoviesFromLocalDataSource(searchTerm)
    }

    override fun insertMovies(movieList: List<Movie>) {

    }

    private fun getMoviesFromLocalDataSource(searchTerm: String): Flowable<List<Movie>> {
        return moviesLocalDataSource.getMovies(searchTerm)
    }

    private fun getMoviesFromRemoteDataSource(searchTerm: String): Flowable<List<Movie>> {
        return moviesRemoteDataSource.getMovies(searchTerm)
    }

}