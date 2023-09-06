package com.example.playlistmaker.player.domain.interfaces

import com.example.playlistmaker.player.data.dto.PlayerTrackDto
import kotlinx.coroutines.flow.Flow

interface AudioPlayerDatabaseRepository {
    suspend fun addPlayerTrackToDatabase(playerTrackDto: PlayerTrackDto)
    suspend fun deletePlayerTrackFromDatabase(playerTrackDto: PlayerTrackDto)
    suspend fun getTracksIdFromDatabase(): Flow<List<Int>>
}