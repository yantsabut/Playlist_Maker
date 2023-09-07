package com.example.playlistmaker.medialibrary.domain.db

import com.example.playlistmaker.medialibrary.data.dto.LibraryTrackDto
import kotlinx.coroutines.flow.Flow


interface LibraryRepository {
    suspend fun getPlayerTracksFromDatabase(): Flow<List<LibraryTrackDto>>
}