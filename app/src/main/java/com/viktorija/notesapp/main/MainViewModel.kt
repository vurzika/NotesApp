package com.viktorija.notesapp.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.viktorija.notesapp.data.NotesRepository
import com.viktorija.notesapp.data.database.AppDatabase
import com.viktorija.notesapp.data.database.Note
import kotlinx.coroutines.launch

class MainViewModel internal constructor(application: Application) : AndroidViewModel(application) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application))

    /**
     * Property to allow observing list of notes available in system
     */
    val notes = notesRepository.getAllNotes()

    /**
     * Method to allow add list with sample data to the database
     */
    fun addSampleNotes() {
        // do in background (approach for any operation that changes data in the database)
        viewModelScope.launch {
            val allNotes = mutableListOf<Note>()

            allNotes.add(Note("A", "abc", false))
            allNotes.add(Note("B", "aaa", false))
            allNotes.add(Note("C", "bbb", true))
            allNotes.add(Note("D", "ccc", false))

            notesRepository.addNotes(allNotes)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            notesRepository.deleteAllNotes()
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
        }
    }

    fun toggleIsImportantNote(noteId: Long) {
        // find note with provided id and toggle it's important value
        notes.value?.first { note -> note.id == noteId }?.let {
            viewModelScope.launch {
                notesRepository.updateIsImportantForNote(it.id, !it.isImportant)
            }
        }
    }

    /**
     * Factory for constructing ViewModel as we need to pass application
     */
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

