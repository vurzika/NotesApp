package com.viktorija.notesapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    // all methods that update/write data use suspend
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNote(note: Note) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<Note>)

    @Query("SELECT * FROM notes WHERE id = :noteId")
    fun getNoteById(noteId : Long) : LiveData<Note>

    // method to get all notes from the database to display them on the screen
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): LiveData<List<Note>>

    // method to get notes with the Important flag from the database
    // SQLite does not have a boolean data type. Room maps it to an INTEGER column, mapping true to 1 and false to 0.
    @Query("SELECT * FROM notes WHERE isImportant=1 ORDER BY id DESC")
    fun getImportantNotes(): LiveData<List<Note>>

    // method to delete all notes
    @Query("DELETE FROM notes")
    fun deleteAllNotes()
}



