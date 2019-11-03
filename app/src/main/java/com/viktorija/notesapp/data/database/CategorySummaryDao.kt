package com.viktorija.notesapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CategorySummaryDao {

    // method to get category summary from the database
    @Query("SELECT * FROM category_summaries ORDER BY id DESC")
    fun getAll(): LiveData<List<CategorySummary>>

    @Query("SELECT * FROM category_summaries WHERE id = :categoryId")
    fun getById(categoryId: Long): LiveData<CategorySummary>

}