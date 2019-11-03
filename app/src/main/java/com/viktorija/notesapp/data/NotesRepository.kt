package com.viktorija.notesapp.data

import com.viktorija.notesapp.data.database.AppDatabase
import com.viktorija.notesapp.data.database.Category
import com.viktorija.notesapp.data.database.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// private constructor - because repository is singleton
class NotesRepository private constructor(private val database: AppDatabase) {

    fun getAllNotes() = database.noteDao.getAll()

    // get one note by id
    fun getNoteById(noteId: Long) = database.noteDao.getNoteById(noteId)


    fun getNotesByCategory(categoryId: Long) = database.noteDao.getNotesByCategory(categoryId)

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
        withContext(Dispatchers.IO) {
            database.noteDao.saveNote(newNote)
        }
    }

    // method to delete note from the database
    suspend fun deleteNote(noteId: Long) {
        withContext(Dispatchers.IO) {
            database.noteDao.deleteNote(noteId)
        }
    }

    // method to update isImportant flag
    suspend fun updateIsImportantForNote(noteId: Long, isImportant: Boolean) {
        withContext(Dispatchers.IO) {
            database.noteDao.updateIsImportantForNote(noteId, isImportant)
        }
    }

    // get list of important notes
    fun getImportantNotes() = database.noteDao.getImportantNotes()


    // Methods related to the Categories

    fun getCategories() = database.categoryDao.getAll()

    // method to get one list by id
    fun getCategoryById(categoryId: Long) = database.categoryDao.getById(categoryId)

    // method to save list to the database
    suspend fun saveCategory(category: Category) {
        withContext(Dispatchers.IO) {
            database.categoryDao.save(category)
        }
    }

    // method to delete list from the database
    suspend fun deleteCategory(categoryId: Long) {
        withContext(Dispatchers.IO) {
            database.categoryDao.delete(categoryId)
        }
    }

    // category summary methods

    fun getCategorySummaries() = database.categorySummaryDao.getAll()

    // get category summary information by category id
    fun getCategorySummaryById(categoryId: Long) = database.categorySummaryDao.getById(categoryId)


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