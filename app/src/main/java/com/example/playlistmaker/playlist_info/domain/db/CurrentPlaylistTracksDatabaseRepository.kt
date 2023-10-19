package com.example.playlistmaker.playlist_info.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface CurrentPlaylistTracksDatabaseRepository {

    suspend fun getTracksForCurrentPlaylist(ids: List<Int>): Flow<List<Track>>

}