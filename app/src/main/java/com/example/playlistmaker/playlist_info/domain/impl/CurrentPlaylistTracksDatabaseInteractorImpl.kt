package com.example.playlistmaker.playlist_info.domain.impl

import com.example.playlistmaker.playlist_info.domain.db.CurrentPlaylistTracksDatabaseInteractor
import com.example.playlistmaker.playlist_info.domain.db.CurrentPlaylistTracksDatabaseRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class CurrentPlaylistTracksDatabaseInteractorImpl(
    private val currentPlaylistTracksDatabaseRepository: CurrentPlaylistTracksDatabaseRepository
) : CurrentPlaylistTracksDatabaseInteractor {
    override suspend fun getTracksForCurrentPlaylist(ids: List<Int>): Flow<List<Track>> {
        return currentPlaylistTracksDatabaseRepository.getTracksForCurrentPlaylist(ids)
    }
}