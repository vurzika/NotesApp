package com.viktorija.notesapp.important

import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.viktorija.notesapp.databinding.ImportantFragmentBinding

class ImportantFragment : Fragment() {

    private lateinit var binding: ImportantFragmentBinding

    // view model setup
    private val viewModel: ImportantViewModel by viewModels {
        ImportantViewModel.Factory( requireNotNull(this.activity).application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflating the layout
        binding = DataBindingUtil.inflate(
            inflater,
            com.viktorija.notesapp.R.layout.important_fragment,
            container,
            false
        )


        // Telling RecyclerView about the Adapter
        val adapter = ImportantNotesListAdapter()

        //Associate the adapter with the RecyclerView.
        binding.notesList.adapter = adapter

        // adding divider
        val divider = DividerItemDecoration(context, ClipDrawable.HORIZONTAL)
        binding.notesList.addItemDecoration(divider)

        // Getting data into the adapter
        // creating an observer on the nights variable
        viewModel.importantNotes.observe(this, Observer {
            // If data is available, letting adapter know that it has new list
            it?.let {
                adapter.notes = it
            }
        })
        // returning the view
        return binding.root
    }
}