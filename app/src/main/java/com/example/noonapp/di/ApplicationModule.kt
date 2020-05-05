package com.example.noonapp.di

import android.content.Context
import androidx.room.Room
import com.example.noonapp.data.MoviesRepository
import com.example.noonapp.data.database.AppDatabase
import com.example.noonapp.data.network.MoviesService
import com.example.noonapp.ui.movies.MoviesRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton


@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {


    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class MoviesRemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class MoviesLocalDataSource


    @JvmStatic
    @MoviesRemoteDataSource
    @Provides
    fun provideMoviesRemoteDataSource(
        moviesService: MoviesService
    ): com.example.noonapp.data.network.MoviesRemoteDataSource {
        return com.example.noonapp.data.network.MoviesRemoteDataSource(moviesService)
    }

    @JvmStatic
    @MoviesLocalDataSource
    @Provides
    fun provideMoviesLocalDataSource(
        database: AppDatabase
    ): com.example.noonapp.data.database.MoviesLocalDataSource {
        val moviesDao = database.moviesDao()
        val searchTermDao = database.searchTermDao()
        return com.example.noonapp.data.database.MoviesLocalDataSource(moviesDao, searchTermDao)
    }



    @JvmStatic
    @Singleton
    @Provides
    fun provideDataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: MoviesRepo): MoviesRepository
}

