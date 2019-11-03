package com.viktorija.notesapp.notes

import android.app.Application
import androidx.lifecycle.*
import com.viktorija.notesapp.data.NotesRepository
import com.viktorija.notesapp.data.database.AppDatabase
import com.viktorija.notesapp.data.database.Note
import kotlinx.coroutines.launch

class NotesListViewModel internal constructor(
    application: Application,
    private val categoryId: Long,
    onlyImportantInd: Boolean
) : AndroidViewModel(application) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application))


    val listTitle: LiveData<String> = when {
        // Using Transformations.map to get title of the category by category
        // LiveData<String> <- LiveData<Category>
        categoryId != 0L -> Transformations.map(notesRepository.getCategorySummaryById(categoryId)) { categorySummary ->
            "${categorySummary?.title?.capitalize()} (${categorySummary?.notesCount})"
        }
        onlyImportantInd -> MutableLiveData("Important Notes")
        else -> MutableLiveData("My Notes")
    }

    /**
     * Property to allow observing list of notes available in system
     */
    val notes = when {
        categoryId != 0L -> notesRepository.getNotesByCategory(categoryId)
        onlyImportantInd -> notesRepository.getImportantNotes()
        else -> notesRepository.getAllNotes()
    }

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

    fun deleteCategory() {
        viewModelScope.launch {
            notesRepository.deleteCategory(categoryId)
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
    class Factory(
        val application: Application,
        private val categoryId: Long,
        private val onlyImportantInd: Boolean
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotesListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotesListViewModel(application, categoryId, onlyImportantInd) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

