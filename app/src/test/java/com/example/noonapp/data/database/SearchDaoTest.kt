package com.example.noonapp.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
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


@RunWith(AndroidJUnit4::class)
@SmallTest
class SearchDaoTest {

    private lateinit var database: AppDatabase


    private var movieTestSubscriber: TestSubscriber<SearchedMovie>? = null

    @Before
    fun initDb() {

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
    fun insertSearchTermAndGetById() {
        val searchTerm = SearchTerm("Matrix")
        database.searchTermDao().insert(searchTerm)
        val searchTermDb = database.searchTermDao().getSearchTerm(searchTerm.searchTerm)

        assertThat<SearchTerm>(searchTermDb as SearchTerm, notNullValue())
        assertThat(searchTermDb.searchTerm, `is`(searchTerm.searchTerm))

    }

}