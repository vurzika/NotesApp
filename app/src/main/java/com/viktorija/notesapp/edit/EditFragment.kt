package com.viktorija.notesapp.edit


import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.viktorija.notesapp.R
import com.viktorija.notesapp.databinding.EditFragmentBinding


class EditFragment : Fragment() {

    // binding variable
    private lateinit var binding: EditFragmentBinding

    // getting arguments passed by navigation
    private val editFragmentArgs by navArgs<EditFragmentArgs>()

    // view model setup for the selected id
    private val viewModel: EditViewModel by viewModels {
        EditViewModel.Factory(requireNotNull(this.activity).application, editFragmentArgs.id)
    }

    // adapter for displaying categories in spinner
    private lateinit var categoryListAdapter: ArrayAdapter<String>

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
        viewModel.note.observe(viewLifecycleOwner, Observer {
            // if we get the data then set text
            it?.let {
                binding.noteTitle.setText(it.title)
                // using .setSelection to put cursor at the end
                binding.noteTitle.setSelection(binding.noteTitle.text.length)

                binding.noteText.setText(it.text)
                binding.noteText.setSelection(binding.noteText.text.length)

                // update selected category to match note selection
                displaySelectedCategoryInDropdown()

                // when item is loaded ask to update menu to set new icon for favorites button
                // recreating menu options to refresh favorites icon
                activity?.invalidateOptionsMenu()
            }
        })

        val spinner = binding.categoriesSpinner

        categoryListAdapter = ArrayAdapter(requireContext(), R.layout.spinner_row)
        spinner.adapter = categoryListAdapter

        // Set list of available categories
        viewModel.categories.observe(viewLifecycleOwner, Observer {
            it?.let {
                categoryListAdapter.clear()

                // Converting List of categories to List of Strings
                val allCategoryTitles = it.map { item -> item.title }.toMutableList()
                // adding first item to the list for case where note doesn't have categories
                allCategoryTitles.add(0, "No Category")

                // add all other categories
                categoryListAdapter.addAll(allCategoryTitles)

                displaySelectedCategoryInDropdown()
            }
        })

        viewModel.eventErrorMessage.observe(viewLifecycleOwner, Observer {
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

    private fun displaySelectedCategoryInDropdown() {
        // if we have note already, select
        viewModel.note.value?.let { note ->
            viewModel.categories.value?.let { categories ->
                val selectedCategory =
                    categories.firstOrNull { category -> category.id == note.categoryId }

                binding.categoriesSpinner.setSelection(
                    when (selectedCategory) {
                        null -> 0
                        else -> categoryListAdapter.getPosition(selectedCategory.title)
                    }
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_edit, menu)
    }

    // When we get the note from view model, update the "star" icon in the menu
    override fun onPrepareOptionsMenu(menu: Menu) {
        // changing menu item based on note status
        val menuItem = menu.findItem(R.id.action_important)
        val note = viewModel.note.value
        if (note != null) {
            // update icon
            menuItem.setIcon(
                when (note.isImportant) {
                    true -> R.drawable.ic_important_selected_toolbar
                    else -> R.drawable.ic_important_not_selected_toolbar
                }
            )
            menuItem.isEnabled = true
        } else {
            menuItem.isEnabled = false
        }

        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val noteTitle: String = binding.noteTitle.text.toString()
                val noteText: String = binding.noteText.text.toString()

                val noteCategoryTitle = when (binding.categoriesSpinner.selectedItemPosition) {
                    0 -> null
                    else -> binding.categoriesSpinner.selectedItem.toString()
                }

                // if saved then return back else stay on screen
                if (viewModel.saveNote(noteTitle, noteText, noteCategoryTitle)) {
                    view?.findNavController()?.popBackStack()
                }
            }
            R.id.action_important -> {
                viewModel.toggleImportantFlagOnNote()
                activity?.invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}