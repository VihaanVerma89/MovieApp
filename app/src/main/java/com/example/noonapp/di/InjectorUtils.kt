/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.noonapp.di

import android.content.Context
import com.example.noonapp.data.database.AppDatabase
import com.example.noonapp.data.database.MoviesLocalDataSource
import com.example.noonapp.data.network.MoviesRemoteDataSource
import com.example.noonapp.data.network.MoviesService
import com.example.noonapp.ui.main.MoviesRepo
import com.example.noonapp.ui.main.MoviesViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getMoviesRepository(context: Context): MoviesRepo {
        val appDatabase = AppDatabase.getInstance(context)
        val moviesDao = appDatabase.moviesDao()
        val moviesLocalDataSource = MoviesLocalDataSource(moviesDao)
        val movieService = getMovieService()
        val moviesRemoteDataSource = MoviesRemoteDataSource(movieService)
        return MoviesRepo(moviesLocalDataSource, moviesRemoteDataSource)
    }

    private fun getMovieService(): MoviesService {
        val retrofit = getRetrofit()
        return retrofit.create(MoviesService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        val BASE_URL = "http://www.omdbapi.com/"


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit
    }


    fun provideMoviesViewModelFactory(context: Context): MoviesViewModelFactory {
        val repository = getMoviesRepository(context)
        return MoviesViewModelFactory(repository)
    }

}
