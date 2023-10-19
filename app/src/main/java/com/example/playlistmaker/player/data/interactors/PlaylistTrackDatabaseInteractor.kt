package com.example.playlistmaker.player.data.interactors

import com.example.playlistmaker.search.domain.models.Track

interface PlaylistTrackDatabaseInteractor {
    suspend fun insertTrackToPlaylistTrackDatabase(track: Track)

    suspend fun deletePlaylistTrackFromDatabase(track: Track)

    suspend fun deletePlaylistTrackFromDatabaseById(id: Int)

}