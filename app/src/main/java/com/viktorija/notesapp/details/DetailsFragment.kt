package com.viktorija.notesapp.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.viktorija.notesapp.R
import com.viktorija.notesapp.databinding.DetailsFragmentBinding

class DetailsFragment : Fragment() {

    // binding variable
    private lateinit var binding: DetailsFragmentBinding

    // getting arguments passed by navigation
    private val detailsFragmentArgs by navArgs<DetailsFragmentArgs>()

    // view model setup for the selected id
    private val viewModel: DetailsViewModel by viewModels {
        DetailsViewModel.Factory(requireNotNull(this.activity).application, detailsFragmentArgs.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflating the layout
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.details_fragment,
            container,
            false
        )
        // When we get the note from view model, update text fields
        viewModel.note.observe(this, Observer {
            // if we get the data then set text
            it?.let {
                binding.noteTitle.text =it.title
                binding.noteText.text = it.text
            }
        })
        return binding.root
    }

}