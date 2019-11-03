package com.viktorija.notesapp.data.database

import androidx.room.DatabaseView

// Combining two tables - Categories and Notes into one and getting count of notes by category
@DatabaseView(
    viewName = "category_summaries",
    value = "SELECT categories.id AS id, categories.title AS title, COUNT(notes.category_id) AS notesCount " +
            "FROM categories LEFT JOIN notes ON notes.category_id = categories.id " +
            "GROUP BY categories.id"
)
data class CategorySummary(

    var id: Long,

    var title: String,

    var notesCount: Int
)