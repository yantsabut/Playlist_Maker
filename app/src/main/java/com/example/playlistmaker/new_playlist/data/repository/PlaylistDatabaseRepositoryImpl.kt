package com.example.playlistmaker.new_playlist.data.repository

import com.example.playlistmaker.database.PlaylistDatabase
import com.example.playlistmaker.new_playlist.domain.db.PlaylistDatabaseRepository
import com.example.playlistmaker.new_playlist.domain.models.Playlist
import com.example.playlistmaker.new_playlist.domain.models.mapToPlaylistEntity

class PlaylistDatabaseRepositoryImpl (private val playlistDatabase: PlaylistDatabase) :
    PlaylistDatabaseRepository {
    override suspend fun insertPlaylistToDatabase(playlist: Playlist) {
        playlistDatabase.playlistDao().insertPlaylist(playlist.mapToPlaylistEntity())
    }

}