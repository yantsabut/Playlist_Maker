package com.example.playlistmaker.player.domain.interactors

import com.example.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseInteractor
import com.example.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseRepository
import com.example.playlistmaker.search.domain.models.Track

class PlaylistTrackDatabaseInteractorImpl(
    private val playlistTrackDatabaseRepository: PlaylistTrackDatabaseRepository
) : PlaylistTrackDatabaseInteractor {
    override suspend fun insertTrackToPlaylistTrackDatabase(track: Track) {
        playlistTrackDatabaseRepository.insertTrackToPlaylistTrackDatabase(track)
    }
}