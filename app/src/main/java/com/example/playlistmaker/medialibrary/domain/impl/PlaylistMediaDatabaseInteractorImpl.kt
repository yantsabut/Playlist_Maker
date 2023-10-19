package com.example.playlistmaker.medialibrary.domain.impl

import com.example.playlistmaker.medialibrary.domain.db.PlaylistMediaDatabaseInteractor
import com.example.playlistmaker.medialibrary.domain.db.PlaylistMediaDatabaseRepository
import com.example.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistMediaDatabaseInteractorImpl (
    private val playlistMediaDatabaseRepository: PlaylistMediaDatabaseRepository
) : PlaylistMediaDatabaseInteractor {
    override suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>> {
        return playlistMediaDatabaseRepository.getPlaylistsFromDatabase()
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistMediaDatabaseRepository.deletePlaylist(playlist)
    }
}