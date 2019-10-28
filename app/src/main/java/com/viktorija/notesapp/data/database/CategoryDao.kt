package com.viktorija.notesapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {

    // method to add list
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(list: Category) : Long

    // method to get all lists from the database to display them on the screen
    @Query("SELECT * FROM categories ORDER BY id DESC")
    fun getAll(): LiveData<List<Category>>

    // method to get one list by id
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getById(categoryId : Long) : LiveData<Category>

    // method to delete list
    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun delete(categoryId: Long)
}