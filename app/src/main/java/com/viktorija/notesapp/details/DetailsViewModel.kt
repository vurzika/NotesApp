package com.viktorija.notesapp.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viktorija.notesapp.data.NotesRepository
import com.viktorija.notesapp.data.database.AppDatabase

class DetailsViewModel internal constructor(application: Application, noteId: Long) : AndroidViewModel(application){

    // The data source this ViewModel will fetch results from.
    private val notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application))

    // getting note from repository
    val note = notesRepository.getNoteById(noteId)

    /**
     * Factory for constructing ViewModel as we need to pass application
     */
    class Factory(val application: Application, var noteId: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailsViewModel(application, noteId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}