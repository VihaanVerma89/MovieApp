package com.example.noonapp.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import androidx.annotation.Keep

@Keep
@JsonClass(generateAdapter = true)
data class MoviesResponse(
    @Json(name = "Response")
    val response: String,
    @Json(name = "Search")
    val search: List<Search>,
    @Json(name = "totalResults")
    val totalResults: String
)