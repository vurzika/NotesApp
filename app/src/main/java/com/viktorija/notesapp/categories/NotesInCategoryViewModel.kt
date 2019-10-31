package com.viktorija.notesapp.categories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.viktorija.notesapp.data.NotesRepository
import com.viktorija.notesapp.data.database.AppDatabase
import kotlinx.coroutines.launch

class NotesInCategoryViewModel internal constructor(application: Application, categoryId: Long) : AndroidViewModel(application) {

    private val notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application))

    val notes = notesRepository.getNotesByCategory(categoryId)

    // method to delete note (for Swipe to delete)
    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
        }
    }


    /**
     * Factory for constructing ViewModel as we need to pass application
     */
    class Factory(val application: Application, private val categoryId: Long ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotesInCategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotesInCategoryViewModel(application, categoryId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}