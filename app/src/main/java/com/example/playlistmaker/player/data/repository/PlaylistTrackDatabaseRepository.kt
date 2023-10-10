package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.search.domain.models.Track

interface PlaylistTrackDatabaseRepository {
    suspend fun insertTrackToPlaylistTrackDatabase(track: Track)
}