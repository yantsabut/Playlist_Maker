package com.example.playlistmaker.player.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.new_playlist.domain.models.Playlist
import com.example.playlistmaker.player.domain.models.PlayerTrack
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.player.presentation.STATE_PAUSED
import com.example.playlistmaker.player.presentation.STATE_PLAYING
import com.example.playlistmaker.player.presentation.state_clases.FavouriteTrackState
import com.example.playlistmaker.player.ui.adapters.PlaylistBottomSheetAdapter
import com.example.playlistmaker.root.listeners.BottomNavigationListener
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.mapToPlayerTrack
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class PlayerFragment: Fragment() {

    private var bottomNavigationListener: BottomNavigationListener? = null

    private lateinit var binding: FragmentPlayerBinding

    private lateinit var backArrow: ImageView
    private lateinit var coverImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var collectionName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var playButton: ImageButton
    private lateinit var durationInTime: TextView
    private lateinit var favouriteButton: ImageButton
    private lateinit var addToPlaylistButton: ImageButton
    private lateinit var createPlaylistButton: Button
    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var overlay: View

    var track: Track? = null

    var allowToEmit = false

    var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private var adapter: PlaylistBottomSheetAdapter? = null

    private lateinit var playerTrack: PlayerTrack

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(playerTrack)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        } else {
            throw IllegalArgumentException("Activity must implement BottomNavigationListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allowToEmit = false

        adapter = PlaylistBottomSheetAdapter(requireContext()) {playlist ->
            clickOnItem(playlist)
        }

        backArrow = binding.backArrowPlaylist
        coverImage = binding.coverMax
        trackName = binding.trackName
        artistName = binding.artistName
        duration = binding.durationName
        collectionName = binding.albumName
        year = binding.yearName
        genre = binding.genreName
        country = binding.countryName
        playButton = binding.playButton
        durationInTime = binding.durationInTime
        favouriteButton = binding.favouriteButton
        addToPlaylistButton = binding.addToPlaylistButton
        createPlaylistButton = binding.createPlaylistBottomSheetButton
        overlay = binding.overlay
        playlistRecyclerView = binding.playlistsBottomSheetRecyclerview

        playlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        playlistRecyclerView.adapter = adapter

        bottomSheetContainer = binding.playlistBottomSheet

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }

        })

        bottomSheetBehavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        createPlaylistButton.setOnClickListener {
            viewModel.release()
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }

        backArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        favouriteButton.setOnClickListener {
            if (viewModel.checkValueFromIsFavourite()) {
                favouriteButton.setImageResource(R.drawable.like_button)
                viewModel.assignValueToIsFavourite(false)
                lifecycleScope.launch {
                    viewModel.deletePlayerTrackFromFavourites()
                }
            } else {
                favouriteButton.setImageResource(R.drawable.like_button_marked)
                viewModel.assignValueToIsFavourite(true)
                lifecycleScope.launch {
                    viewModel.addPlayerTrackToFavourites()
                }
            }
        }

        track = requireArguments().getSerializableExtra(CURRENT_TRACK, Track::class.java)

        playerTrack = track!!.mapToPlayerTrack()

        viewModel.preparePlayer()
        viewModel.assignValToPlayerTrackForRender()

        if (playerTrack.previewUrl == null) {
            playButton.isEnabled = false
        }

        viewModel.checkTrackIsFavourite()

        viewModel.playerTrackForRender.observe(viewLifecycleOwner) { playerTrack ->
            render(playerTrack)
        }

        viewModel.playerState.observe(viewLifecycleOwner) { statePlaying ->

            when (statePlaying) {
                STATE_PLAYING -> playButton.setImageResource(R.drawable.pause_button)
                STATE_PAUSED -> playButton.setImageResource(R.drawable.play_button)
            }

        }

        viewModel.isCompleted.observe(viewLifecycleOwner) { isCompleted ->
            if (isCompleted) {
                durationInTime.text = getString(R.string.player_start_position)
                playButton.setImageResource(R.drawable.play_button)
            }
        }

        viewModel.formattedTime.observe(viewLifecycleOwner) { trackTime ->
            durationInTime.text = trackTime
        }

        viewModel.favouriteTrack.observe(viewLifecycleOwner) { favouriteTrackState ->
            renderFavouriteButton(favouriteTrackState)
        }

        viewModel.playlistsFromDatabase.observe(viewLifecycleOwner) { listOfPlaylists ->
            addPlaylistsToBottomSheetRecyclerView(listOfPlaylists)
        }

        viewModel.checkIsTrackInPlaylist.observe(viewLifecycleOwner) { playlistTrackState ->
            if (allowToEmit) {
                if (playlistTrackState.trackIsInPlaylist) {
                    val toastPhrase = getString(R.string.track_already_added) + " ${playlistTrackState.nameOfPlaylist}"
                    Toast.makeText(requireContext(), toastPhrase, Toast.LENGTH_SHORT).show()
                } else {
                    val toastPhrase = getString(R.string.track_added) + " ${playlistTrackState.nameOfPlaylist}"
                    Toast.makeText(requireContext(), toastPhrase, Toast.LENGTH_SHORT).show()
                    viewModel.getPlaylists()
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

        }

        viewModel.allowToCleanTimer = true

    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation(true)
        viewModel.getPlaylists()
        if (viewModel.allowToCleanTimer) {
            durationInTime.text = "00:00"
        }
    }

    override fun onStop() {
        super.onStop()
        hideBottomNavigation(false)
    }

    private fun clickOnItem(playlist: Playlist) {

        allowToEmit = true

        viewModel.checkAndAddTrackToPlaylist(playlist, track)

    }

    private fun renderFavouriteButton(favouriteTrackState: FavouriteTrackState) {

        when {

            favouriteTrackState.isLoading -> {
                favouriteButton.isEnabled = false
            }

            else -> {
                favouriteButton.isEnabled = true

                if (favouriteTrackState.isFavourite == true) {
                    favouriteButton.setImageResource(R.drawable.like_button_marked)
                } else {
                    favouriteButton.setImageResource(R.drawable.like_button)
                }
            }
        }

    }

    fun addPlaylistsToBottomSheetRecyclerView(listOfPlaylists: List<Playlist>) {
        adapter?.playlists?.clear()
        adapter?.playlists?.addAll(listOfPlaylists)
        adapter?.notifyDataSetChanged()
    }

    private fun hideBottomNavigation(isHide: Boolean) {
        bottomNavigationListener?.toggleBottomNavigationViewVisibility(!isHide)
    }

    private fun render(track: PlayerTrack) {
        trackName.text = viewModel.checkEmptinessOrNull(track.trackName)
        artistName.text = viewModel.checkEmptinessOrNull(track.artistName)
        duration.text = viewModel.checkEmptinessOrNull(track.trackTime)
        collectionName.text = viewModel.checkEmptinessOrNull(track.collectionName)
        year.text = viewModel.checkEmptinessOrNull(track.releaseDate)
        genre.text = viewModel.checkEmptinessOrNull(track.primaryGenreName)
        country.text = viewModel.checkEmptinessOrNull(track.country)

        Glide.with(this)
            .load(track.artworkUrl)
            .placeholder(R.drawable.track_placeholder_max)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius)))
            .into(coverImage)
    }

    private fun <T : Serializable?> Bundle.getSerializableExtra(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializable(key, m_class)!!
        else
            this.getSerializable(key) as T
    }

    companion object {
        private const val CURRENT_TRACK = "CURRENT_TRACK"

        fun createArgs(track: Track): Bundle {
            return bundleOf(CURRENT_TRACK to track)
        }
    }

}
