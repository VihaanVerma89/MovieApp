package com.example.noonapp.data.database

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.noonapp.MovieTestUtils
import com.example.noonapp.data.models.SearchTerm
import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.subscribers.TestSubscriber
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@MediumTest
@Config(sdk = [Build.VERSION_CODES.P])
class MoviesLocalDataSourceTest {

    private lateinit var localDataSource: MoviesLocalDataSource
    private lateinit var database: AppDatabase

    private var movieTestSubscriber: TestSubscriber<SearchedMovie>? = null

    @Before
    fun initDb() {

        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        movieTestSubscriber = TestSubscriber()

        localDataSource = MoviesLocalDataSource(database.moviesDao(), database.searchTermDao())
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetMovies() {
        val searchTerm = "Batman"
        val searchTermObj = SearchTerm(searchTerm)
        val moviesList = MovieTestUtils.getMoviesList(searchTerm)

        val searchedMovie = SearchedMovie(searchTermObj, moviesList)
        localDataSource.insertMovies(searchedMovie)
        val searchedMovieDb = localDataSource.getMovies(searchTerm).blockingFirst()
        assertEquals(searchedMovie, searchedMovieDb)
    }


}