package com.example.playlistmaker.medialibrary.domain.db

import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import kotlinx.coroutines.flow.Flow


interface LibraryDatabaseInteractor {
    suspend fun getPlayerTracksFromDatabase(): Flow<List<LibraryTrack>>
}