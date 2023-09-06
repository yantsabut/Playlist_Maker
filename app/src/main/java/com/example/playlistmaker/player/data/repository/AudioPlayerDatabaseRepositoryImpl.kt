package com.example.playlistmaker.player.data.repository

import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.player.data.converters.PlayerTrackDbConverter
import com.example.playlistmaker.player.data.dto.PlayerTrackDto
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AudioPlayerDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playerTrackDbConverter: PlayerTrackDbConverter
) : AudioPlayerDatabaseRepository {

    override suspend fun addPlayerTrackToDatabase(playerTrackDto: PlayerTrackDto) {
        appDatabase.trackDao().insertTrack(playerTrackDbConverter.map(playerTrackDto))
    }

    override suspend fun deletePlayerTrackFromDatabase(playerTrackDto: PlayerTrackDto) {
        appDatabase.trackDao().deleteTrack(playerTrackDbConverter.map(playerTrackDto))
    }

    override suspend fun getTracksIdFromDatabase(): Flow<List<Int>> = flow {
        emit(appDatabase.trackDao().getTracksId())
    }

}