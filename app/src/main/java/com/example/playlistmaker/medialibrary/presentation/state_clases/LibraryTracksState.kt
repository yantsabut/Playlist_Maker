package com.example.playlistmaker.medialibrary.presentation.state_clases

import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack

data class LibraryTracksState(
    val libraryTracks: List<LibraryTrack>,
    val isLoading: Boolean
)
