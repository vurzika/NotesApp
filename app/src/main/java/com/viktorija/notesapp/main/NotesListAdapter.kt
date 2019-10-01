package com.viktorija.notesapp.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.viktorija.notesapp.R
import com.viktorija.notesapp.data.database.Note

class NotesListAdapter : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    var notes = listOf<Note>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        // creating the view by asking the layoutinflater to inflate it
        // The third argument needs to be false, because RecyclerView adds this item to the
        // view hierarchy for you when it's time.
        val view = layoutInflater
            .inflate(R.layout.note_list_item, parent, false)

        // returning a TextItemViewHolder made with view
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val item = notes[position]

        viewHolder.noteTitle.text = item.title
        viewHolder.noteText.text = item.text

        viewHolder.importantIndicatorIcon.setImageResource(when (item.isImportant) {
            true -> R.drawable.ic_favirote_selected
            else -> R.drawable.ic_favorite_not_selected
        })


        viewHolder.itemView.setOnClickListener {
            it.findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToEditFragment(item.id))

        }

//        // todo:
//        viewHolder.imageView.setOnClickListener{
//            importantNotesViewModel.toggleImportant()
//        }


    }

    override fun getItemCount() = notes.size


    // Create ViewHolder class that extends RecyclerView.ViewHolder.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Getting references to the views that this ViewHolder will update
        val noteTitle: TextView = itemView.findViewById(R.id.note_title)
        val noteText: TextView = itemView.findViewById(R.id.note_text)
        val importantIndicatorIcon : ImageView = itemView.findViewById(R.id.note_important)

    }

}