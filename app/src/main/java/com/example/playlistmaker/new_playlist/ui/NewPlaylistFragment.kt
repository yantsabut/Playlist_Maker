package com.example.playlistmaker.new_playlist.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.new_playlist.domain.models.Playlist
import com.example.playlistmaker.new_playlist.presentation.NewPlaylistViewModel
import com.example.playlistmaker.root.listeners.BottomNavigationListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment: BaseFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                checkForDialogOutput()
            }

        })

        initViews()

        newPlayListButton.isEnabled = false

        editNameEditText.addTextChangedListener(textWatcher)

        val pickMedia = pickMediaCommon


        backArrowImageView.setOnClickListener {
            checkForDialogOutput()
        }

        loadImageImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        newPlayListButton.setOnClickListener {

            val name = editNameEditText.text.toString()

            val filepath = if (uriOfImage != null) viewModel.getNameForFile(editNameEditText.text.toString()) else ""

            val playlist = Playlist(
                name = name,
                description = editDescriptionEditText.text.toString(),
                filePath = filepath,
                listOfTracksId = "",
                amountOfTracks = 0,
                insertTimeStamp = System.currentTimeMillis()
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.insertPlaylistToDatabase(playlist)
            }

            uriOfImage?.let { saveImageToPrivateStorage(uri = it, nameOfFile = filepath) }

            val toastPhrase = getString(R.string.add_playlist) + " $name"

            Toast.makeText(requireContext(), toastPhrase, Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }

    }

    private fun checkForDialogOutput() {
        if (imageIsLoaded ||
            editNameEditText.text.toString().isNotEmpty() ||
            editDescriptionEditText.text.toString().isNotEmpty()
        ) {
            showDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setTitle(getString(R.string.complete_playlist))
            .setMessage(getString(R.string.data_lost))
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->

            }
            .setPositiveButton(getString(R.string.complete)) { dialog, which ->
                findNavController().navigateUp()
            }
            .show()
    }

}