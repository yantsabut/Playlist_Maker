package com.example.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsMedialibraryBinding
import com.example.playlistmaker.medialibrary.presentation.MedialibraryPlaylistsViewModel
import com.example.playlistmaker.medialibrary.presentation.state_clases.PlaylistState
import com.example.playlistmaker.medialibrary.ui.adapters.PlaylistAdapter
import com.example.playlistmaker.new_playlist.domain.models.Playlist
import com.example.playlistmaker.playlist_info.ui.PlaylistInfoFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedialibraryPlaylistsFragment : Fragment() {

    private val viewModel: MedialibraryPlaylistsViewModel by viewModel()

    private var _binding: FragmentPlaylistsMedialibraryBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true

    private var adapter: PlaylistAdapter? = null

    private lateinit var createPlaylistButton: Button
    private lateinit var emptyPlaylistsPlaceholder: ConstraintLayout
    private lateinit var playlistProgressbar: ProgressBar
    private lateinit var playlistRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PlaylistAdapter(requireContext()) { playlist ->
                clickOnItem(playlist)
        }

        createPlaylistButton = binding.createPlaylistButton
        emptyPlaylistsPlaceholder = binding.emptyPlaylistsPlaceholder
        playlistProgressbar = binding.playlistProgressbar
        playlistRecyclerView = binding.playlistRecyclerView

        playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistRecyclerView.adapter = adapter

        createPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_medialibraryFragment_to_newPlaylistFragment)
        }

        viewModel.databasePlaylistState.observe(viewLifecycleOwner) { playlistState ->
            render(playlistState)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun clickOnItem(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_medialibraryFragment_to_playlistInfoFragment,
            PlaylistInfoFragment.createArgs(playlist)
        )
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

    private fun View.show(isVisible: Boolean) {
        if (isVisible) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    private fun showLoader() {
        emptyPlaylistsPlaceholder.show(false)
        playlistRecyclerView.show(false)
        playlistProgressbar.show(true)
    }

    private fun showPlaceholder() {
        playlistRecyclerView.show(false)
        playlistProgressbar.show(false)
        emptyPlaylistsPlaceholder.show(true)
    }

    private fun showContent() {
        playlistProgressbar.show(false)
        emptyPlaylistsPlaceholder.show(false)
        playlistRecyclerView.show(true)
    }

    private fun render(playlistState: PlaylistState) {

        when(playlistState) {
            is PlaylistState.Loading -> {
                showLoader()
            }

            is PlaylistState.Success -> {
                if (playlistState.data.isEmpty()) {
                    showPlaceholder()
                } else {
                    adapter?.playlists?.clear()
                    adapter?.playlists?.addAll(playlistState.data)
                    adapter?.notifyDataSetChanged()

                    showContent()
                }
            }
        }
    }

    companion object {
        fun newInstance(): MedialibraryPlaylistsFragment {
            return MedialibraryPlaylistsFragment()
        }

        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}