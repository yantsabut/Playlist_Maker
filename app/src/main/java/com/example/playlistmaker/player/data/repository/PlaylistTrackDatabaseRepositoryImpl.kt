package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.mapToPlaylistTrackEntity

class PlaylistTrackDatabaseRepositoryImpl (
    private val playlistTrackDatabase: AppDatabase
) : PlaylistTrackDatabaseRepository {

    override suspend fun insertTrackToPlaylistTrackDatabase(track: Track) {
        playlistTrackDatabase.playlistTrackDao().insertTrack(track.mapToPlaylistTrackEntity())
    }

    override suspend fun deletePlaylistTrackFromDatabase(track: Track) {
        playlistTrackDatabase.playlistTrackDao().deletePlaylistTrack(track.mapToPlaylistTrackEntity(newTimeStamp = false))
    }

    override suspend fun deletePlaylistTrackFromDatabaseById(id: Int) {
        playlistTrackDatabase.playlistTrackDao().deleteTrackById(id)
    }
}