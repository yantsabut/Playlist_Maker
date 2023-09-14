package com.example.playlistmaker.favourite.domain

import com.example.playlistmaker.player.domain.converters.PlayerTrackDataConverter
import com.example.playlistmaker.favourite.data.FavouriteRepository
import com.example.playlistmaker.player.domain.models.PlayerTrack
import kotlinx.coroutines.flow.Flow

class FavouriteInteractorImpl(
    private val audioPlayerDatabaseRepository: FavouriteRepository,
    private val playerTrackDataConverter: PlayerTrackDataConverter
) : FavouriteInteractor {

    override suspend fun addPlayerTrackToFavourite(
        playerTrack: PlayerTrack,
        insertionTimeStamp: Long
    ) {
        audioPlayerDatabaseRepository.addPlayerTrackToFavourite(
            playerTrackDataConverter.map(
                playerTrack,
                insertionTimeStamp
            )
        )
    }

    override suspend fun deletePlayerTrackFromFavourite(playerTrack: PlayerTrack) {
        audioPlayerDatabaseRepository.deletePlayerTrackFromFavourite(
            playerTrackDataConverter.map(
                playerTrack
            )
        )
    }

    override suspend fun getTracksIdFromFavourite(): Flow<List<Int>> {
        return audioPlayerDatabaseRepository.getTracksIdFromFavourite()
    }
}
