package com.viktorija.notesapp.data.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

// data class for Entity

@Entity(tableName = "notes")
data class Note(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    var title: String,

    var text: String,

    var isImportant: Boolean

) {
    // @Ignore constructor for creating new objects manually
    @Ignore
    constructor(title: String, text: String, isImportant: Boolean) : this(0, title, text, isImportant)
}

