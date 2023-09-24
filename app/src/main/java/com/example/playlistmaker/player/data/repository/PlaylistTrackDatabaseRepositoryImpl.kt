package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.database.PlaylistTrackDatabase
import com.example.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.mapToPlaylistTrackEntity

class PlaylistTrackDatabaseRepositoryImpl (
    private val playlistTrackDatabase: PlaylistTrackDatabase
) : PlaylistTrackDatabaseRepository {

    override suspend fun insertTrackToPlaylistTrackDatabase(track: Track) {
        playlistTrackDatabase.playlistTrackDao().insertTrack(track.mapToPlaylistTrackEntity())
    }
}