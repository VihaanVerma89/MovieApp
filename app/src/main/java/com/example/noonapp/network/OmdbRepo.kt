package com.example.noonapp.network

import com.example.noonapp.BuildConfig
import com.example.noonapp.models.MoviesResponse
import io.reactivex.Single

class OmdbRepo : BaseRepo() {
    private val service: OmdbService = getRetrofit().create(OmdbService::class.java)
    private val key = BuildConfig.CONSUMER_KEY
    fun getMoviesResponse(searchTerm: String): Single<MoviesResponse> {
        return service.getMoviesResponse(searchTerm, key)
    }
}