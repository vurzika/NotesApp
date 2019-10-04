package com.viktorija.notesapp.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viktorija.notesapp.data.database.Note
import com.viktorija.notesapp.databinding.NoteListItemBinding

// Modify the constructor of the NotesListAdapter class to receive a
// val clickListener: NoteClickListener - to handle clicks on the note items

// Change the class signature of SleepNightAdapter to extend ListAdapter.
// Add SleepNight as the first argument to the ListAdapter, before SleepNightAdapter.ViewHolder.
// Add SleepNightDiffCallback() as a parameter to the constructor.
// The ListAdapter will use this to figure out what changed in the list.
class NotesListAdapter(private val clickListener: NoteClickListener) :
    ListAdapter<Note, NotesListAdapter.ViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.bind(item, clickListener)
    }

    // Create ViewHolder class that extends RecyclerView.ViewHolder.
    class ViewHolder(val binding: NoteListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var noteId: Long = 0
            private set

        fun bind(item: Note, clickListener: NoteClickListener) {
            noteId = item.id

            binding.note = item
            binding.clickListener = clickListener
        }

        // same as static method in java
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

// New class -  NoteClickListener to handle clicks
class NoteClickListener(
    val clickListener: (noteId: Long) -> Unit,
    val clickStarListener: (noteId: Long) -> Unit
) {

    fun onClick(note: Note) = clickListener(note.id)

    fun onStarClick(note: Note) = clickStarListener(note.id)
}

// Implementing SleepNightDiffCallback
class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {

    // DiffUtil uses these two methods to figure out how the list and items have changed.
    // code that tests whether the two passed-in SleepNight items, oldItem and newItem, are the same
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    // code that checks whether oldItem and newItem contain the same data; that is, whether they are equal
    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}