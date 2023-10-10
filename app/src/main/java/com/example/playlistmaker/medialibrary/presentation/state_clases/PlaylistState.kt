package com.example.playlistmaker.medialibrary.presentation.state_clases

import com.example.playlistmaker.new_playlist.domain.models.Playlist

sealed class PlaylistState {
    object Loading : PlaylistState()
    class Success(val data: List<Playlist>) : PlaylistState()
}