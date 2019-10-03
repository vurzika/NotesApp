package com.viktorija.notesapp.important

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viktorija.notesapp.data.NotesRepository
import com.viktorija.notesapp.data.database.AppDatabase


class ImportantViewModel internal constructor(application: Application) : AndroidViewModel(application) {

    private val notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application))

    val importantNotes = notesRepository.getImportantNotes()

    /**
     * Factory for constructing ViewModel as we need to pass application
    */
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ImportantViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ImportantViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}