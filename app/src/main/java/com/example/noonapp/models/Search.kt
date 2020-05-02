package com.example.noonapp.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep

@Keep
@JsonClass(generateAdapter = true)
data class Search(
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