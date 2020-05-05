package com.example.noonapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noonapp.data.database.Tables

@Entity(tableName = Tables.SEARCH_TERMS)
data class SearchTerm(
//    var searchTermId: Long =0,
    @PrimaryKey
    val searchTerm: String
)
