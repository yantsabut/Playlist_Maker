package com.example.playlistmaker.player.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track

interface PlaylistTrackDatabaseRepository {
    suspend fun insertTrackToPlaylistTrackDatabase(track: Track)
}