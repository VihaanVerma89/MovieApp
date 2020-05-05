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

import androidx.room.*
import com.example.noonapp.data.database.Tables
import com.example.noonapp.data.models.SearchTerm
import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.Flowable

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface SearchTermDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchTerm: SearchTerm): Long

    @Transaction
    @Query("SELECT * FROM ${Tables.SEARCH_TERMS} where searchTerm = :searchTerm")
    fun getSearchedMovie(searchTerm: String): Flowable<SearchedMovie>


}
