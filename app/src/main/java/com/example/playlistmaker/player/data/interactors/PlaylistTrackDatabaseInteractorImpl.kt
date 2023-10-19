package com.example.playlistmaker.player.data.interactors

import com.example.playlistmaker.player.data.interactors.PlaylistTrackDatabaseInteractor
import com.example.playlistmaker.player.data.repository.PlaylistTrackDatabaseRepository
import com.example.playlistmaker.search.domain.models.Track

class PlaylistTrackDatabaseInteractorImpl(
    private val playlistTrackDatabaseRepository: PlaylistTrackDatabaseRepository
) : PlaylistTrackDatabaseInteractor {
    override suspend fun insertTrackToPlaylistTrackDatabase(track: Track) {
        playlistTrackDatabaseRepository.insertTrackToPlaylistTrackDatabase(track)
    }
    override suspend fun deletePlaylistTrackFromDatabase(track: Track) {
        playlistTrackDatabaseRepository.deletePlaylistTrackFromDatabase(track)
    }

    override suspend fun deletePlaylistTrackFromDatabaseById(id: Int) {
        playlistTrackDatabaseRepository.deletePlaylistTrackFromDatabaseById(id)
    }
}