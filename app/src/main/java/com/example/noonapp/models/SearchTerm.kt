package com.example.noonapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noonapp.data.database.Tables

@Entity(tableName = Tables.SEARCH_TERMS)
data class SearchTerm(
//    var searchTermId: Long = -1,
//    var searchTermId: Long ,
    val searchTerm: String
)
{

    @PrimaryKey(autoGenerate = true)
    var searchTermId: Long =0
}