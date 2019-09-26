package com.viktorija.notesapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    // all methods that update/write data use suspend
    @Insert
    suspend fun saveNote(note: Note) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<Note>)

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getNoteById(noteId : Long) : LiveData<Note>

    // method to get all drinks from the DrinkDatabase to display them on the screen
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): LiveData<List<Note>>

    // method to delete all notes
    @Query("DELETE FROM notes")
    fun deleteAllNotes()
}



