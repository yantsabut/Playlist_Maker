package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavouritesMedialibraryBinding
import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import com.example.playlistmaker.medialibrary.presentation.MedialibraryFavouritesViewModel
import com.example.playlistmaker.medialibrary.presentation.state_clases.LibraryTracksState
import com.example.playlistmaker.medialibrary.ui.adapters.LibraryTrackAdapter
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.playlist_info.ui.PlaylistInfoFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedialibraryFavouritesFragment : Fragment() {

    private val viewModel:  MedialibraryFavouritesViewModel by viewModel()

    private var _binding: FragmentFavouritesMedialibraryBinding? = null
    private val binding get() = _binding!!

    private var adapter: LibraryTrackAdapter? = null

    private var isClickAllowed = true

    private lateinit var emptyLibraryPlaceholder: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var libraryRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LibraryTrackAdapter { libraryTrack ->
                clickOnItem(libraryTrack)
        }

        emptyLibraryPlaceholder = binding.emptyLibraryPlaceholder
        progressBar = binding.progressBar
        libraryRecyclerView = binding.libraryRecyclerView

        libraryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        libraryRecyclerView.adapter = adapter


        viewModel.databaseTracksState.observe(viewLifecycleOwner) { libraryTrackState ->
            render(libraryTrackState)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
        isClickAllowed = true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }

        return current
    }


    private fun clickOnItem(libraryTrack: LibraryTrack) {
        val track = viewModel.convertLibraryTrackToTrack(libraryTrack)

        findNavController().navigate(
            R.id.action_medialibraryFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )

    }

    private fun render(libraryTracksState: LibraryTracksState) {

        when {

            libraryTracksState.isLoading -> {
                showLoader(true)
                showPlaceHolder(false)
                showContent(false)
            }

            else -> {
                showLoader(false)

                if (libraryTracksState.libraryTracks.isEmpty()) {
                    showPlaceHolder(true)
                    showContent(false)
                } else {
                    adapter?.tracks?.clear()
                    adapter?.tracks?.addAll(libraryTracksState.libraryTracks)
                    adapter?.notifyDataSetChanged()

                    showContent(true)
                    showPlaceHolder(false)
                }
            }
        }

    }

    private fun showLoader(isVisible: Boolean) {
        if (isVisible) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showPlaceHolder(isVisible: Boolean) {
        if (isVisible) {
            emptyLibraryPlaceholder.visibility = View.VISIBLE
        } else {
            emptyLibraryPlaceholder.visibility = View.GONE
        }
    }

    private fun showContent(isVisible: Boolean) {
        if (isVisible) {
            libraryRecyclerView.visibility = View.VISIBLE
        } else {
            libraryRecyclerView.visibility = View.GONE
        }
    }


    companion object {

        private const val CLICK_DEBOUNCE_DELAY = 1000L

        fun newInstance(): MedialibraryFavouritesFragment {
            return MedialibraryFavouritesFragment()
        }
    }

}