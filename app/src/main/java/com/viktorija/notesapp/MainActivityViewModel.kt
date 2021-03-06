package com.viktorija.notesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.viktorija.notesapp.data.NotesRepository
import com.viktorija.notesapp.data.database.AppDatabase
import com.viktorija.notesapp.data.database.Category
import kotlinx.coroutines.launch

class MainActivityViewModel internal constructor(application: Application) :
    AndroidViewModel(application) {

    private val notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application))

    val categories = notesRepository.getCategorySummaries()

    fun saveCategory(category: Category) {
        viewModelScope.launch {
            notesRepository.saveCategory(category)
        }
    }

    /**
     * Factory for constructing ViewModel as we need to pass application
     */
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}