package com.example.noonapp.network

import com.example.noonapp.models.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbService {

    @GET("/")
    fun getMoviesResponse(@Query("s") searchTerm: String, @Query("apikey") apiKey: String): Single<MoviesResponse>

}