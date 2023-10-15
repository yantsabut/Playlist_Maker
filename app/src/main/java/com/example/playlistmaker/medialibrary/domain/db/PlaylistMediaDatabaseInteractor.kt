package com.example.playlistmaker.medialibrary.domain.db

import com.example.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistMediaDatabaseInteractor {

    suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>>

    suspend fun deletePlaylist(playlist: Playlist)

}