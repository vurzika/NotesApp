package com.viktorija.notesapp.data.database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

// data class for Entity

@Entity(
    tableName = "notes",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("category_id"),
        onDelete = CASCADE
    )]
)
data class Note(

    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "category_id")
    var categoryId: Long?,

    var title: String,

    var text: String,

    var isImportant: Boolean

) {
    // @Ignore constructor for creating new objects manually
    @Ignore
    constructor(title: String, text: String, isImportant: Boolean) : this(
        0,
        null,
        title,
        text,
        isImportant
    )
}

