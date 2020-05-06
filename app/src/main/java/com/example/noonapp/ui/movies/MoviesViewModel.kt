package com.example.noonapp.ui.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noonapp.data.RequestResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesViewModel @Inject constructor(val repo: MoviesRepo) : ViewModel() {

    companion object {
        val TAG = MoviesViewModel::class.java.name
    }

    val getMoviesMld = MutableLiveData<RequestResult<Any>>()

    private val compositeDisposable = CompositeDisposable()
    fun getMovies(searchTerm: String) {
        getMoviesMld.value = RequestResult.Loading(Unit)
        compositeDisposable.clear()
        val subscribe = repo.getMovies(searchTerm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d(TAG, "getMovies: onNext $it")
                if (it is Throwable) {
                    getMoviesMld.value = RequestResult.Error(it)
                } else {
                    getMoviesMld.value = RequestResult.Success(it)
                }
            }, {
                Log.d(TAG, "getMovies: onError $it")
                getMoviesMld.value = RequestResult.Error(it)
            }, {
                Log.d(TAG, "getMovies: onComplete")
            })
        compositeDisposable.add(subscribe)
    }

}