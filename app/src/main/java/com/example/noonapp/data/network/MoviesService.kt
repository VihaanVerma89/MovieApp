package com.example.noonapp.data.network

import com.example.noonapp.data.models.MoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    @GET("/")
    fun getMoviesResponse(@Query("s") searchTerm: String, @Query("apikey") apiKey: String): Single<MoviesResponse>

}