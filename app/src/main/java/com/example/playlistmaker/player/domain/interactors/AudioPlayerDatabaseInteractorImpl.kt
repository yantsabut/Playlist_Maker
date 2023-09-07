package com.example.playlistmaker.player.domain.interactors

import com.example.playlistmaker.player.domain.converters.PlayerTrackDataConverter
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerDatabaseInteractor
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerDatabaseRepository
import com.example.playlistmaker.player.domain.models.PlayerTrack
import kotlinx.coroutines.flow.Flow

class AudioPlayerDatabaseInteractorImpl(
    private val audioPlayerDatabaseRepository: AudioPlayerDatabaseRepository,
    private val playerTrackDataConverter: PlayerTrackDataConverter
) : AudioPlayerDatabaseInteractor {

    override suspend fun addPlayerTrackToDatabase(
        playerTrack: PlayerTrack,
        insertionTimeStamp: Long
    ) {
        audioPlayerDatabaseRepository.addPlayerTrackToDatabase(
            playerTrackDataConverter.map(
                playerTrack,
                insertionTimeStamp
            )
        )
    }

    override suspend fun deletePlayerTrackFromDatabase(playerTrack: PlayerTrack) {
        audioPlayerDatabaseRepository.deletePlayerTrackFromDatabase(
            playerTrackDataConverter.map(
                playerTrack
            )
        )
    }

    override suspend fun getTracksIdFromDatabase(): Flow<List<Int>> {
        return audioPlayerDatabaseRepository.getTracksIdFromDatabase()
    }
}
