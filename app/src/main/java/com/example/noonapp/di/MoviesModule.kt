package com.example.noonapp.di

import androidx.lifecycle.ViewModel
import com.example.noonapp.ui.movies.MoviesFragment
import com.example.noonapp.ui.movies.MoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MoviesModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun moviesFragment(): MoviesFragment


    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindViewModel(viewmodel: MoviesViewModel): ViewModel
}