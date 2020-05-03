package com.example.noonapp.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noonapp.data.network.RequestResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesViewModel(val repo: MoviesRepo) : ViewModel() {

    companion object {
        val TAG = MoviesViewModel::class.java.name
    }

    val getMoviesMld = MutableLiveData<RequestResult<Any>>()

    private val compositeDisposable = CompositeDisposable()
    fun getMovies(searchTerm: String) {
        compositeDisposable.clear()
        val subscribe = repo.getMovies(searchTerm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "getMovies: onNext $it")
                getMoviesMld.value = RequestResult.Success(it)
            }, {
                Log.d(TAG, "getMovies: onError $it")
                getMoviesMld.value = RequestResult.Error(it)
            }, {
                Log.d(TAG, "getMovies: onComplete")
            })
        compositeDisposable.add(subscribe)
    }

}