package com.example.playlistmaker.playlist_info.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.example.playlistmaker.new_playlist.domain.models.Playlist
import com.example.playlistmaker.new_playlist.ui.EditPlaylistFragment
import com.example.playlistmaker.player.ui.PlayerFragment
import com.example.playlistmaker.playlist_info.presentation.PlaylistInfoViewModel
import com.example.playlistmaker.playlist_info.presentation.containers.PlaylistInfoContainer
import com.example.playlistmaker.root.listeners.BottomNavigationListener
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistInfoFragment: Fragment() {

    var isClickAllowed = true

    private var bottomNavigationListener: BottomNavigationListener? = null

    private lateinit var binding: FragmentPlaylistInfoBinding

    var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    var playlist: Playlist? = null

    private val onLongClickAction = fun(track: Track): Boolean {
        showDeleteTrackDialog(track)
        return true
    }

    val adapter = TrackAdapter(onLongClickAction, true) { track ->
        onClickItem(track)
    }

    private val viewModel: PlaylistInfoViewModel by viewModel()

    // Views of main screen
    private lateinit var backArrowImageView: ImageView
    private lateinit var playlistCoverImageView: ImageView
    private lateinit var nameOfPlaylistTextView: TextView
    private lateinit var yearOfPlaylistCreationTextView: TextView
    private lateinit var totalAmountOfMinutesTextView: TextView
    private lateinit var totalNumberOfTracksTextView: TextView
    private lateinit var sharePlaylistImageView: ImageView
    private lateinit var menuOfPlaylistImageView: ImageView

    // Views of Playlist Bottom Sheet
    private lateinit var playlistInfoBottomSheetLinearLayout: LinearLayout
    private lateinit var playlistInfoBottomSheetRecyclerView: RecyclerView

    // Views of Menu Bottom Sheet
    private lateinit var playlistMenuBottomSheetLinearLayout: LinearLayout
    private lateinit var playlistCoverBottomSheetImageView: ImageView
    private lateinit var nameOfPlaylistBottomSheetTextView: TextView
    private lateinit var totalNumberOfTracksBottomSheetTextView: TextView
    private lateinit var sharePlaylistBottomSheetFrameLayout: FrameLayout
    private lateinit var editPlaylistBottomSheetFrameLayout: FrameLayout
    private lateinit var deletePlaylistBottomSheetFrameLayout: FrameLayout

    // Overlay
    private lateinit var overlay: View

    // Lifecycle Callbacks
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

    override fun onResume() {
        super.onResume()
        hideBottomNavigation(true)
    }

    override fun onStop() {
        isClickAllowed = true
        super.onStop()
        hideBottomNavigation(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Views of main screen
        backArrowImageView = binding.playlistInfoBackarrowImageview
        playlistCoverImageView = binding.playlistInfoCoverImageview
        nameOfPlaylistTextView = binding.nameOfPlaylistInfoTextview
        yearOfPlaylistCreationTextView = binding.yearOfPlaylistInfoTextview
        totalAmountOfMinutesTextView = binding.totalMinutesPlaylistInfoTextview
        totalNumberOfTracksTextView = binding.amountOfTracksPlaylistInfoTextview
        sharePlaylistImageView = binding.playlistInfoShareImageview
        menuOfPlaylistImageView = binding.playlistInfoMenuImageview


        // Views of Playlist Bottom Sheet
        playlistInfoBottomSheetLinearLayout = binding.playlistInfoBottomSheet
        playlistInfoBottomSheetRecyclerView = binding.playlistInfoBottomSheetRecyclerview


        // Views of Menu Bottom Sheet
        playlistMenuBottomSheetLinearLayout = binding.playlistMenuBottomSheet
        playlistCoverBottomSheetImageView = binding.playlistInfoCoverMinImageview
        nameOfPlaylistBottomSheetTextView = binding.nameOfPlaylistInfoMinTextview
        totalNumberOfTracksBottomSheetTextView = binding.amountOfTracksPlaylistInfoMinTextview
        sharePlaylistBottomSheetFrameLayout = binding.sharePlaylistFramelayout
        editPlaylistBottomSheetFrameLayout = binding.editPlaylistFramelayout
        deletePlaylistBottomSheetFrameLayout = binding.deletePlaylistFramelayout

        overlay = binding.overlay

        // Recycler View
        playlistInfoBottomSheetRecyclerView.adapter = adapter
        playlistInfoBottomSheetRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Bottom Sheet Behavior
        bottomSheetBehavior = BottomSheetBehavior.from(playlistMenuBottomSheetLinearLayout).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

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

        // Get Playlist
        if (viewModel.updatedPlaylist == null) {
            playlist = requireArguments().getSerializableExtra(CURRENT_PLAYLIST, Playlist::class.java)
        } else {
            playlist = viewModel.updatedPlaylist
        }

        if (playlist!!.listOfTracksId.isEmpty()) {
            Toast.makeText(requireContext(), "В плейлисте пока нет треков", Toast.LENGTH_SHORT).show()
        }

        //Rendering
        renderWithSerializableData()
        viewModel.getTracksFromDatabaseForCurrentPlaylist(viewModel.convertStringToList(playlist!!.listOfTracksId))


        // Button Listeners
        backArrowImageView.setOnClickListener {

            findNavController().popBackStack(R.id.mediaLibraryFragment ,false)

        }

        sharePlaylistImageView.setOnClickListener {
            if (clickDebounce()) {
                sharePlaylist()
            }
        }

        menuOfPlaylistImageView.setOnClickListener {
            if (clickDebounce()) {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        sharePlaylistBottomSheetFrameLayout.setOnClickListener {
            sharePlaylist()
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        }

        editPlaylistBottomSheetFrameLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistInfoFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlist!!)
            )
        }

        deletePlaylistBottomSheetFrameLayout.setOnClickListener {
            if (playlist != null) {
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
                showDeletePlaylistDialog()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                findNavController().popBackStack(R.id.mediaLibraryFragment ,false)

            }

        })

        //Observers
        viewModel.tracksForCurrentPlaylist.observe(viewLifecycleOwner) {playlistInfoContainer ->
            renderWithDatabaseData(playlistInfoContainer)
        }

    }

    //Private functions
    private fun sharePlaylist() {
        if (playlist != null) {
            if (playlist!!.listOfTracksId.isEmpty()) {
                showShareEmptyPlaylistDialog()
            } else {
                sendMessageToExternalResources()
            }
        }
    }

    private fun showDeleteTrackDialog(track:Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setTitle(getString(R.string.delete_track_title))
            .setMessage(getString(R.string.delete_track_message))
            .setNegativeButton(getString(R.string.no)) { dialog, which ->

            }
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                deleteTrackFromPlaylist(track)
            }
            .show()
    }

    private fun showShareEmptyPlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setMessage(getString(R.string.share_empty_playlist))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
            }
            .show()
    }

    private fun showDeletePlaylistDialog() {

        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setTitle(getString(R.string.delete_playlist_title))
            .setMessage(getString(R.string.delete_playlist_ask))
            .setNegativeButton(getString(R.string.no)) { dialog, which ->

            }
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                viewModel.deletePlaylist(playlist)
                findNavController().navigateUp()
            }
            .show()
    }

    private fun sendMessageToExternalResources() {

        var message = viewModel.getMessageForExternalResources(playlist)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun deleteTrackFromPlaylist(track:Track) {

        val listStringOfTrackIds: String? = playlist?.listOfTracksId
        val listOfTrackIds = listStringOfTrackIds?.let { viewModel.convertStringToList(it) }

        listOfTrackIds?.remove(track.trackId)

        val newListStringOfTrackIds = listOfTrackIds?.let {viewModel.convertListToString(listOfTrackIds)}

        val updatedPlaylist = newListStringOfTrackIds?.let { playlist?.copy(listOfTracksId = it, amountOfTracks = playlist!!.amountOfTracks - 1)}

        playlist = updatedPlaylist

        viewModel.updatedPlaylist = updatedPlaylist

        if (updatedPlaylist != null) {
            viewModel.updatePlaylist(updatedPlaylist)
        }

        viewModel.checkAndDeleteTrackFromPlaylistTrackDatabase(track)

        if (listOfTrackIds != null) {
            viewModel.getTracksFromDatabaseForCurrentPlaylist(listOfTrackIds)
        }

        totalNumberOfTracksTextView.text =
            playlist?.let { viewModel.pluralizeWord(it.amountOfTracks, "трек") }

    }

    private fun onClickItem(track: Track) {
        findNavController().navigate(
            R.id.action_playlistInfoFragment_to_playerFragment,
            PlayerFragment.createArgs(track)
        )
    }

    private fun renderWithSerializableData() {
        if (playlist != null) {


            if (playlist!!.filePath.isNotEmpty()) {
                playlistCoverImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                playlistCoverImageView.setImageURI(getUriOfImageFromStorage(playlist!!.filePath))

                playlistCoverBottomSheetImageView.setImageURI(getUriOfImageFromStorage(playlist!!.filePath))
            } else {
                playlistCoverImageView.setImageResource(R.drawable.track_placeholder_max)
                playlistCoverBottomSheetImageView.setImageResource(R.drawable.placeholder_playlist)
            }

            nameOfPlaylistTextView.text = playlist!!.name
            yearOfPlaylistCreationTextView.text = playlist!!.description

            totalNumberOfTracksTextView.text = viewModel.pluralizeWord(playlist!!.amountOfTracks, "трек")

            nameOfPlaylistBottomSheetTextView.text = playlist!!.name

            totalNumberOfTracksBottomSheetTextView.text = viewModel.pluralizeWord(playlist!!.amountOfTracks, "трек")

        } else {
            Log.d("DEBUG", "playlist is null")
        }
    }

    private fun renderWithDatabaseData(playlistInfoContainer: PlaylistInfoContainer) {

        viewModel.listOfCurrentTracks.clear()
        viewModel.listOfCurrentTracks.addAll(playlistInfoContainer.playlistTracks)

        adapter.tracks.clear()
        adapter.tracks.addAll(playlistInfoContainer.playlistTracks)
        adapter.notifyDataSetChanged()

        totalAmountOfMinutesTextView.text = playlistInfoContainer.totalTime
    }

    private fun <T : Serializable?> Bundle.getSerializableExtra(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializable(key, m_class)!!
        else
            this.getSerializable(key) as T
    }

    private fun getUriOfImageFromStorage(fileName: String): Uri {
        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, fileName)
        return file.toUri()
    }

    private fun hideBottomNavigation(isHide: Boolean) {
        bottomNavigationListener?.toggleBottomNavigationViewVisibility(!isHide)
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
    companion object {
        private const val CURRENT_PLAYLIST = "CURRENT_PLAYLIST"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(playlist: Playlist): Bundle {
            return bundleOf(CURRENT_PLAYLIST to playlist)
        }
    }
}