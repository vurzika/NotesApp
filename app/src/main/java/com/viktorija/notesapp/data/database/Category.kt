package com.viktorija.notesapp.data.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    var title: String

) {
    // @Ignore constructor for creating new objects manually
    @Ignore
    constructor(title: String) : this(0, title)
}