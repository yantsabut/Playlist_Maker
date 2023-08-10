package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouritesMedialibraryBinding
import com.example.playlistmaker.medialibrary.presentation.MedialibraryFavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedialibraryFavouritesFragment : Fragment() {

    private val viewModel:  MedialibraryFavouritesViewModel by viewModel()

    private var _binding: FragmentFavouritesMedialibraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(): MedialibraryFavouritesFragment {
            return MedialibraryFavouritesFragment()
        }
    }

}