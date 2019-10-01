package com.viktorija.notesapp.edit


import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.viktorija.notesapp.R
import com.viktorija.notesapp.databinding.EditFragmentBinding

//import sun.jvm.hotspot.utilities.IntArray


class EditFragment : Fragment() {

    // binding variable
    // todo?
    private lateinit var binding: EditFragmentBinding

    // getting arguments passed by navigation
    private val editFragmentArgs by navArgs<EditFragmentArgs>()

    // view model setup for the selected id
    private val viewModel: EditViewModel by viewModels {
        EditViewModel.Factory(requireNotNull(this.activity).application, editFragmentArgs.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflating the layout
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_fragment, container, false)

        // Changing ActionBar title
        if (viewModel.editMode) {
            (activity as AppCompatActivity)
                .supportActionBar?.title = "Edit note"
        } else {
            (activity as AppCompatActivity)
                .supportActionBar?.title = "New note"
        }

        // When we get the note from view model, update text fields
        viewModel.note.observe(this, Observer {
            // if we get the data then set text
            it?.let {
                binding.noteTitle.setText(it.title)
                binding.noteTitle.setSelection(binding.noteTitle.getText().length);

                binding.noteText.setText(it.text)
                binding.noteText.setSelection(binding.noteText.getText().length);

                // when item is loaded ask to update menu to set new icon for favorites button
                activity?.invalidateOptionsMenu()
            }
        })

        viewModel.eventErrorMessage.observe(this, Observer {
            it?.let {
                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.resetEventErrorMessage()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        // changing menu item based on note status
        viewModel.note.value?.let {
            val menuItem = menu.findItem(R.id.action_important)

            // get icon
            val icon = ContextCompat.getDrawable(context!!, when (it.isImportant) {
                true -> R.drawable.ic_favirote_selected
                else -> R.drawable.ic_favorite_not_selected
            })

            // change icon tint color to white
            DrawableCompat.setTint(icon!!, Color.WHITE);

            // update icon
            menuItem.icon = icon
        }

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val noteTitle: String = binding.noteTitle.text.toString()
                val noteText: String = binding.noteText.text.toString()

                // if saved then return back
                if (viewModel.saveNote(noteTitle, noteText)) {
                    view?.findNavController()?.popBackStack()
                }
            }
            R.id.action_important -> {
                viewModel.toggleImportantFlagOnNote()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}