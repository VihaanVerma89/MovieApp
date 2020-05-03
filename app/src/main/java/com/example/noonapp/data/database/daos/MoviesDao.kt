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

package com.example.noonapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.noonapp.models.Movie

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface MoviesDao {
//    @Query("SELECT * FROM ${Tables.MOVIES}")
//    fun getMovies(searchTerm: String): Flowable<List<Movie>>


//    @Transaction
//    @Query("SELECT * FROM ${Tables.SEARCH_TERMS}")
//    fun getSearchedMovies(searchTerm: String): Flowable<List<SearchedMovie>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>): List<Long>

}
