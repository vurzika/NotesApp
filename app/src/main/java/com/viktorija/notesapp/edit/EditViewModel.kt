package com.viktorija.notesapp.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.viktorija.notesapp.data.NotesRepository
import com.viktorija.notesapp.data.database.AppDatabase
import com.viktorija.notesapp.data.database.Note
import com.viktorija.notesapp.details.DetailsViewModel
import kotlinx.coroutines.launch

class EditViewModel internal constructor(application: Application) : AndroidViewModel(application){


    // The data source this ViewModel will fetch results from.
    private val notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application))

    // Saving new note
    fun saveNote(newNote: Note) {
        viewModelScope.launch {
            notesRepository.saveNote(newNote)
        }
    }

    /**
     * Factory for constructing ViewModel as we need to pass application
     */
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}