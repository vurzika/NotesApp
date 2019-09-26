package com.viktorija.notesapp.data

import com.viktorija.notesapp.data.database.Note
import com.viktorija.notesapp.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// private constructor - because repository is singleton
class NotesRepository private constructor(private val database: AppDatabase) {

    fun getAllNotes() = database.noteDao.getAll()

    // get one note by id
    fun getNoteById(noteId: Long) = database.noteDao.getNoteById(noteId);

    // using suspend as doing async write to database
    // method to add sample data (from menu)
    // allows to save to the database multiple notes at the same time
    suspend fun addNotes(notes: List<Note>) {
        withContext(Dispatchers.IO) {
            database.noteDao.insertAll(notes)
        }
    }

    // method to delete all notes
    suspend fun deleteAllNotes() {
        withContext(Dispatchers.IO) {
            database.noteDao.deleteAllNotes()
        }
    }

    // method to save note to the database
    suspend fun saveNote(newNote: Note) {
        withContext(Dispatchers.IO){
            database.noteDao.saveNote(newNote)
        }
    }

    // singleton for repository
    companion object {

        @Volatile
        private var instance: NotesRepository? = null

        fun getInstance(database: AppDatabase) =
            instance ?: synchronized(this) {
                instance ?: NotesRepository(database).also { instance = it }
            }
    }
}