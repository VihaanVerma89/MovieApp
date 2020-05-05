package com.example.noonapp.ui.movies

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.example.noonapp.R
import kotlinx.android.synthetic.main.main_activity.*


class MoviesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_containerl_fl, MoviesFragment.newInstance())
                .commitNow()
        }
        init()
    }

    private fun init() {
        initToolbar()
    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_movies, menu)
        return true
    }
}
