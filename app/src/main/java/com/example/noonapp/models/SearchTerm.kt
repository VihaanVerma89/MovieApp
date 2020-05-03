package com.example.noonapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noonapp.data.database.Tables

@Entity(tableName = Tables.SEARCH_TERMS)
data class SearchTerm(
    @PrimaryKey(autoGenerate = true)
    var searchTermId: Long =0,
    val searchTerm: String
)
