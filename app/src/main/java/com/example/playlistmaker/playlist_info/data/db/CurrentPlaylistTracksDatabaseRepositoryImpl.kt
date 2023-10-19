package com.example.playlistmaker.playlist_info.data.db

import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.playlist_info.domain.db.CurrentPlaylistTracksDatabaseRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.mapToTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrentPlaylistTracksDatabaseRepositoryImpl(private val playlistTrackDatabase: AppDatabase) :
    CurrentPlaylistTracksDatabaseRepository {
    override suspend fun getTracksForCurrentPlaylist(ids: List<Int>): Flow<List<Track>> {
        return flow {
            val currentPlaylistTracks = playlistTrackDatabase.playlistTrackDao().getTracksByListIds(ids)
            emit(currentPlaylistTracks.map { playlistTrackEntity -> playlistTrackEntity.mapToTrack() })
        }
    }
}