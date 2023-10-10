package com.example.playlistmaker.new_playlist.data.repository

import com.example.playlistmaker.new_playlist.domain.models.Playlist

interface PlaylistDatabaseInteractor {

    suspend fun insertPlaylistToDatabase(playlist: Playlist)
}