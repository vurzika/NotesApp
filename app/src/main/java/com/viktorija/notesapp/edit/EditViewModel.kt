package com.viktorija.notesapp.edit

import android.app.Application
import androidx.lifecycle.*
import com.viktorija.notesapp.data.NotesRepository
import com.viktorija.notesapp.data.database.AppDatabase
import com.viktorija.notesapp.data.database.Note
import kotlinx.coroutines.launch

class EditViewModel internal constructor(application: Application, noteId: Long) :
    AndroidViewModel(application) {


    // The data source this ViewModel will fetch results from.
    private val notesRepository = NotesRepository.getInstance(AppDatabase.getInstance(application))

    // backing property for note variable
    private var _note: LiveData<Note>
    val note: LiveData<Note>
        get() = _note

    // Creating errorMessage as backing property
    // we use this to communicate to UI that currently there is error to display
    // UI must observe this property and show toast every time when there was an error
    private val _eventErrorMessage = MutableLiveData<String>()
    val eventErrorMessage: LiveData<String>
        get() = _eventErrorMessage

    // boolean. If noteId is not null - we are in edit mode
    val editMode = noteId != 0L

    init {
        if (editMode) {
            // if edit mode then load from database
            _note = notesRepository.getNoteById(noteId)
        } else {
            // initialize with default note values
            _note = MutableLiveData<Note>(Note("", "", false))
        }

    }

    // Saving note object that is currently owned by view model
    // (could be both new note or note that was previously loaded from database)
    fun saveNote(newTitle: String, newText: String) : Boolean {
        val title = newTitle.trim()
        val text = newText.trim()

        // validate that they are not empty
        // if they are empty - update error message and return false for saveNote method
        // if they are not empty - save and return true for saveNote method

        if (title.isNotEmpty() && text.isNotEmpty()) {
            val newNote = _note.value

            newNote?.let {
                it.title = title.trim()
                it.text = text.trim()

                viewModelScope.launch {
                    notesRepository.saveNote(it)
                }
            }

            return true
        } else {
            _eventErrorMessage.value = "Error with saving the note. All the fields must be filled out"

            return false
        }
    }

    fun resetEventErrorMessage() {
        _eventErrorMessage.value = null
    }

    // todo: chto delaet etot metod? po strochkam? zachem if else?
    fun toggleImportantFlagOnNote() {
        val note = _note.value

        note?.let {

            if (editMode) {
                // todo: ??
                // opposite value for note
                it.isImportant = !it.isImportant

                viewModelScope.launch {
                    notesRepository.saveNote(it)
                }
                // todo: v kakom sluchae eto vipolnjaetsja?
            } else {
                _eventErrorMessage.value = "Note must be saved first"
            }
        }
    }

    /**
     * Factory for constructing ViewModel as we need to pass application
     */
    class Factory(val application: Application, val noteId: Long) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditViewModel(application, noteId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}