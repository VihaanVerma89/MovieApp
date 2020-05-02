package com.example.noonapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noonapp.models.MoviesResponse
import com.example.noonapp.network.RequestResult
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MoviesViewModel : ViewModel() {

    val repo = MoviesRepo()

    val getMoviesMld = MutableLiveData<RequestResult<Any>>()
    fun getMovies(searchTerm: String) {
        repo.getMoviesResponse(searchTerm)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                getMoviesMld.value = RequestResult.Success(it)
            }, {
                getMoviesMld.value = RequestResult.Error(it)
            })
    }

}