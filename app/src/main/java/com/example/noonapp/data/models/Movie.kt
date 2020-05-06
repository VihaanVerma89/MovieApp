package com.example.noonapp.data.models


import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noonapp.data.database.Tables
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
@Entity(tableName = Tables.MOVIES)
data class Movie(
    var searchTerm: String="",
    @PrimaryKey
    @Json(name = "imdbID")
    val imdbID: String,
    @Json(name = "Poster")
    val poster: String,
    @Json(name = "Title")
    val title: String,
    @Json(name = "Type")
    val type: String,
    @Json(name = "Year")
    val year: String
)
