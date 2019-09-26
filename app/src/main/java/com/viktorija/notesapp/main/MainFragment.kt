package com.viktorija.notesapp.main


import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.viktorija.notesapp.databinding.MainFragmentBinding
import kotlinx.android.synthetic.main.main_fragment.*



class MainFragment : Fragment() {

    // binding variable
    private lateinit var binding: MainFragmentBinding

    // view model setup
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory( requireNotNull(this.activity).application )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflating the layout
        binding = DataBindingUtil.inflate(
            inflater,
            com.viktorija.notesapp.R.layout.main_fragment,
            container,
            false
        )

        // Telling RecyclerView about the Adapter
        val adapter = NotesListAdapter()

        //Associate the adapter with the RecyclerView.
        binding.notesList.adapter = adapter

        // todo: Add divider
        val divider = DividerItemDecoration(context, HORIZONTAL)
        binding.notesList?.addItemDecoration(divider)


        // Getting data into the adapter
        // creating an observer on the nights variable
        viewModel.notes.observe(this, Observer {
            // If data is available, letting adapter know that it has new list
            it?.let {
                adapter.notes = it
            }
        })


        // Navigating to Edit fragment
        binding.fab.setOnClickListener{ view: View ->
            view.findNavController().navigate(MainFragmentDirections.actionMainFragmentToEditFragment())
        }

        // Indicate that we have menu to setup
        setHasOptionsMenu(true)

        // returning the view
        return binding.root
    }

    // Menu related methods
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(com.viktorija.notesapp.R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.viktorija.notesapp.R.id.action_add_sample_data -> {
                viewModel.addSampleNotes()
                return true
            }
            com.viktorija.notesapp.R.id.action_delete_all -> {
                viewModel.deleteAllNotes()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}