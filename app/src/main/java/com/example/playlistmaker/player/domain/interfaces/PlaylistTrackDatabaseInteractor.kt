package com.example.playlistmaker.player.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track

interface PlaylistTrackDatabaseInteractor {
    suspend fun insertTrackToPlaylistTrackDatabase(track: Track)
}