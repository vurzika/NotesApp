package com.viktorija.notesapp.main


import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.viktorija.notesapp.R
import com.viktorija.notesapp.common.NoteClickListener
import com.viktorija.notesapp.common.NotesListAdapter
import com.viktorija.notesapp.databinding.MainFragmentBinding


class MainFragment : Fragment() {

    // binding variable
    private lateinit var binding: MainFragmentBinding

    // view model setup
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory(requireNotNull(this.activity).application)
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
        val adapter = NotesListAdapter(NoteClickListener {
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToEditFragment(it)
            )
        })

        //Associate the adapter with the RecyclerView.
        binding.notesList.adapter = adapter

        // adding divider
        val divider = DividerItemDecoration(context, HORIZONTAL)
        binding.notesList.addItemDecoration(divider)


        // Navigating to Edit fragment
        binding.fab.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(MainFragmentDirections.actionMainFragmentToEditFragment())
        }

        // (7) Getting data into the adapter
        // creating an observer on the notes variable
        viewModel.notes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Indicate that we have menu to setup
        setHasOptionsMenu(true)

        // returning the view
        return binding.root
    }

    // Menu related methods
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_sample_data -> {
                viewModel.addSampleNotes()
                return true
            }
            R.id.action_delete_all -> {
                viewModel.deleteAllNotes()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}