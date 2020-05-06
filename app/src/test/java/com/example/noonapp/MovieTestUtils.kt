package com.example.noonapp

import com.example.noonapp.data.models.Movie

class MovieTestUtils {
    companion object {

        fun getMoviesList(searchTerm: String): ArrayList<Movie> {
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

        fun getMovie(
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
}