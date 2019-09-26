package com.viktorija.notesapp.edit


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.NavUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.viktorija.notesapp.R
import com.viktorija.notesapp.data.database.Note
import com.viktorija.notesapp.databinding.EditFragmentBinding


class EditFragment : Fragment() {

    // binding variable
    private lateinit var binding: EditFragmentBinding

    // view model setup for the selected id
    private val viewModel: EditViewModel by viewModels {
        EditViewModel.Factory(requireNotNull(this.activity).application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        // inflating the layout
        binding = DataBindingUtil.inflate(inflater,
            R.layout.edit_fragment, container, false )


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                view?.findNavController()?.popBackStack()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun saveNote() {
        val noteTitle: String = binding.noteTitle.text.toString().trim()
        val noteText: String = binding.noteText.text.toString().trim()

        if (noteTitle.isNotEmpty() && noteText.isNotEmpty()) {
            val newNote = Note(noteTitle, noteText)
            viewModel.saveNote(newNote)
        } else if (noteTitle.isEmpty() || noteText.isEmpty()) {
            Toast.makeText(
                context,
                "Error with saving the note. All the fields must be filled out",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}