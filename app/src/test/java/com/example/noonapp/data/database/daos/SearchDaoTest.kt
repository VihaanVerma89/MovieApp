package com.example.noonapp.data.database.daos

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.noonapp.MovieTestUtils
import com.example.noonapp.data.database.AppDatabase
import com.example.noonapp.data.models.SearchTerm
import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.subscribers.TestSubscriber
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@SmallTest
@Config(sdk = [Build.VERSION_CODES.P])
class SearchDaoTest {

    private lateinit var database: AppDatabase


    private var movieTestSubscriber: TestSubscriber<SearchedMovie>? = null

    @Before
    fun initDb() {

        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        movieTestSubscriber = TestSubscriber()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertSearchTermAndGet() {
        val searchTerm = "Matrix"
        insertSearchTerm(searchTerm)
        val searchTermDb = database.searchTermDao().getSearchTerm(searchTerm)
        assertThat<SearchTerm>(searchTermDb as SearchTerm, notNullValue())
        assertThat(searchTermDb.searchTerm, `is`(searchTerm))
    }

    private fun insertSearchTerm(searchTerm: String) {
        val searchTerm = SearchTerm(searchTerm)
        database.searchTermDao().insert(searchTerm)
    }


    @Test
    fun insertSearchTermsAndGet() {
        val searchTermsInsertList =
            listOf<String>("Matrix", "Batman", "Aevengers", "The Last Samurai")
        val searchTermDao = database.searchTermDao()
        searchTermDao.deleteAll()
        for (insertTerm in searchTermsInsertList) {
            val searchTerm = SearchTerm(insertTerm)
            searchTermDao.insert(searchTerm)
        }
        val searchTermsListGet = searchTermDao.getAllSearchTerm()

        for ((index, value) in searchTermsInsertList.withIndex()) {
            val getSearchTerm = searchTermsListGet[index]
            assertThat(value, `is`(getSearchTerm.searchTerm))
        }
    }


    @Test
    fun insertMovies() {
        val moviesDao = database.moviesDao()
        val searchTermDao = database.searchTermDao()

        val searchTerm = "Batman"
        insertSearchTerm(searchTerm)
        val moviesList = MovieTestUtils.getMoviesList(searchTerm)
        moviesDao.insertMovies(moviesList)
        val blockingFirst = searchTermDao.getSearchedMovie(searchTerm).blockingFirst()
        val searchedMovieDb = blockingFirst[0]
        val moviesListDb = searchedMovieDb.movies
        assertEquals(moviesList, moviesListDb)
    }

}