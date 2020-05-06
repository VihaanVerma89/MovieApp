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

package com.example.noonapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noonapp.data.database.daos.MoviesDao
import com.example.noonapp.data.database.daos.SearchTermDao
import com.example.noonapp.data.models.Movie
import com.example.noonapp.data.models.SearchTerm

@Database(entities = [Movie::class, SearchTerm::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun searchTermDao(): SearchTermDao
}
