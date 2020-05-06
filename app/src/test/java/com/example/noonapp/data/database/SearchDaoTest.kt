package com.example.noonapp.data.database

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.noonapp.data.models.Movie
import com.example.noonapp.data.models.SearchTerm
import com.example.noonapp.data.models.SearchedMovie
import io.reactivex.subscribers.TestSubscriber
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
        val moviesList = getMoviesList(searchTerm)
        moviesDao.insertMovies(moviesList)
        searchTermDao.getSearchedMovie(searchTerm).subscribe(movieTestSubscriber)
        val searchedMovie = SearchedMovie(SearchTerm(searchTerm), moviesList)
        movieTestSubscriber?.assertValue(searchedMovie)
    }

    private fun getMoviesList(searchTerm: String): ArrayList<Movie> {
        val listOfMovies = arrayListOf<Movie>()
        val imdbIds =
            arrayListOf<String>("tt0372784", "tt2975590", "tt0096895", "tt0103776", "tt0112462")
        val posters = arrayListOf<String>(
            "https://m.media-amazon.com/images/M/MV5BZmUwNGU2ZmItMmRiNC00MjhlLTg5YWUtODMyNzkxODYzMmZlXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BYThjYzcyYzItNTVjNy00NDk0LTgwMWQtYjMwNmNlNWJhMzMyXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BMTYwNjAyODIyMF5BMl5BanBnXkFtZTYwNDMwMDk2._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BOGZmYzVkMmItM2NiOS00MDI3LWI4ZWQtMTg0YWZkODRkMmViXkEyXkFqcGdeQXVyODY0NzcxNw@@._V1_SX300.jpg",
            "https://m.media-amazon.com/images/M/MV5BNDdjYmFiYWEtYzBhZS00YTZkLWFlODgtY2I5MDE0NzZmMDljXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg"
        )
        val titles = arrayListOf<String>(
            "Batman Begins",
            "Batman v Superman: Dawn of Justice",
            "Batman",
            "Batman Returns",
            "Batman Forever"
        )
        val types = arrayListOf<String>("movie", "movie", "movie", "movie", "movie")
        val year = arrayListOf<String>("2000", "2001", "2002", "2003", "2004")

        for (i in 0 until titles.size) {
            val movie = getMovie(searchTerm, imdbIds[i], posters[i], titles[i], types[i], year[i])
            listOfMovies.add(movie)
        }
        return listOfMovies
    }

    private fun getMovie(
        searchTerm: String,
        imdbId: String,
        poster: String,
        title: String,
        type: String,
        year: String
    ): Movie {
        val movie = Movie(searchTerm, imdbId, poster, title, type, year)
        return movie
    }

}