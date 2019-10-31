package com.viktorija.notesapp.categories

import android.graphics.drawable.ClipDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.viktorija.notesapp.common.NoteClickListener
import com.viktorija.notesapp.common.NotesListAdapter
import com.viktorija.notesapp.databinding.CategoryFragmentBinding

class CategoryFragment : Fragment() {

    private lateinit var binding: CategoryFragmentBinding

    private val categoryFragmentArgs by navArgs<CategoryFragmentArgs>()

    // view model setup
    private val viewModel: NotesInCategoryViewModel by viewModels {
        NotesInCategoryViewModel.Factory(requireNotNull(this.activity).application, categoryFragmentArgs.categoryId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflating the layout
        binding = DataBindingUtil.inflate(
            inflater,
            com.viktorija.notesapp.R.layout.category_fragment,
            container,
            false
        )

//        // Telling RecyclerView about the Adapter
        val adapter = NotesListAdapter(
            NoteClickListener(
                clickListener = {
                    findNavController().navigate(
                        CategoryFragmentDirections.actionCategoryFragmentToEditFragment(it)

                    )
                }, clickStarListener = {
                    // todo:
                    //viewModel.toggleIsImportantNote(it)
                })
        )

        //Associate the adapter with the RecyclerView.
        binding.notesList.adapter = adapter

        // adding divider
        val divider = DividerItemDecoration(context, ClipDrawable.HORIZONTAL)
        binding.notesList.addItemDecoration(divider)

        // Getting data into the adapter
        // creating an observer on the category variable
        viewModel.notes.observe(this, Observer {
            // If data is available, letting adapter know that it has new list
            it?.let {
                adapter.submitList(it)
            }
        })

        initSwipeToDelete()

        // returning the view
        return binding.root
    }

    private fun initSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.Callback() {
            // enable the items to swipe to the left or right
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int =
                makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            // When an item is swiped, remove the item via the view model. The list item will be
            // automatically removed in response, because the adapter is observing the live list.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as NotesListAdapter.ViewHolder).noteId.let {
                    viewModel.deleteNote(it)
                }
            }
        }).attachToRecyclerView(binding.notesList)
    }
}