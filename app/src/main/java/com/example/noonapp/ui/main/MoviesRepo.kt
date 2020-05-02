package com.example.noonapp.ui.main

import com.example.noonapp.models.MoviesResponse
import com.example.noonapp.network.BaseRepo
import com.example.noonapp.network.OmdbRepo
import io.reactivex.Single

class MoviesRepo : BaseRepo() {

    private val omdbRepo = OmdbRepo()

    fun getMoviesResponse(searchTerm:String): Single<MoviesResponse> {
       return  omdbRepo.getMoviesResponse(searchTerm)
    }
}