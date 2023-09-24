package com.example.playlistmaker.medialibrary.domain.db

import com.example.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistMediaDatabaseRepository {

    suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>>
}