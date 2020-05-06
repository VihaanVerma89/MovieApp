package com.example.noonapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noonapp.data.database.Tables

@Entity(tableName = Tables.SEARCH_TERMS)
data class SearchTerm(
    @PrimaryKey
    val searchTerm: String
)
