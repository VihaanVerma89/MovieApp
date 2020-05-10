package com.example.noonapp.ui.movies

import android.util.Log
import com.example.noonapp.data.MoviesRepository
import com.example.noonapp.data.database.DataThrowable
import com.example.noonapp.data.database.MoviesLocalDataSource
import com.example.noonapp.data.models.SearchedMovie
import com.example.noonapp.data.network.MoviesRemoteDataSource
import com.example.noonapp.data.network.NetworkThrowable
import com.example.noonapp.di.ApplicationModule
import com.squareup.moshi.JsonDataException
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MoviesRepo @Inject constructor(
    @ApplicationModule.MoviesLocalDataSource val moviesLocalDataSource: MoviesLocalDataSource,
    @ApplicationModule.MoviesRemoteDataSource val moviesRemoteDataSource: MoviesRemoteDataSource
) : MoviesRepository {

    companion object {
        val TAG = MoviesRepo::class.java.name
    }

    private val compositeDisposable = CompositeDisposable()

    override fun getAndInsertMoviesFromRemoteDataSource(
        emitter: FlowableEmitter<Any>,
        searchTerm: String
    ) {
        val subscribe = getMoviesFromRemoteDataSource(searchTerm)
            .subscribeOn(Schedulers.io())
//            .delay(3, TimeUnit.SECONDS)
            .map {
                insertMovies(it)
                it
            }
            .doOnError {
                println(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(TAG, "getAndSaveMoviesFromRemoteDataSource: onNext $it")
                }, {
                    var any = it
                    if (it is JsonDataException) {
                        // 200 should not return error json but since it is we are wrapping it here.
                        any = DataThrowable(it, searchTerm)
                    } else if (it is Throwable) {
                        any = NetworkThrowable(
                            it,
                            searchTerm
                        )
                    }
                    if (!emitter.isCancelled) {
                        emitter.onNext(any)
                    }
                    Log.d(TAG, "getAndSaveMoviesFromRemoteDataSource: onError $it")
                }, {
                    Log.d(TAG, "getAndSaveMoviesFromRemoteDataSource: onComplete")
                }
            )
        compositeDisposable.add(subscribe)
    }

    override fun getMovies(searchTerm: String): Flowable<Any> {
        return Flowable.create({ emitter ->
            compositeDisposable.clear()
            getMoviesFromLocalDataSource(emitter, searchTerm)
            getAndInsertMoviesFromRemoteDataSource(emitter, searchTerm)
        }, BackpressureStrategy.BUFFER)

    }

    override fun insertMovies(searchedMovie: SearchedMovie) {
        moviesLocalDataSource.insertMovies(searchedMovie)
    }

    override fun getMoviesFromLocalDataSource(
        emitter: FlowableEmitter<Any>,
        searchTerm: String
    ) {
        val subscribe = moviesLocalDataSource.getMovies(searchTerm)
            .doOnNext {
                println(it)
            }
//            .delay(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Log.d(TAG, "getMoviesFromLocalDataSource: onNext $it")
                    if (!emitter.isCancelled) {
                        emitter.onNext(it)
                    }
                }, {
                    Log.d(TAG, "getMoviesFromLocalDataSource: onError $it")
                    if (!emitter.isCancelled) {
                        emitter.onError(it)
                    }
                }, {
                    Log.d(TAG, "getMoviesFromLocalDataSource: onComplete")
                }
            )
        compositeDisposable.add(subscribe)
    }

    override fun getMoviesFromRemoteDataSource(searchTerm: String): Flowable<SearchedMovie> {
        return moviesRemoteDataSource.getMovies(searchTerm)
    }

}