package com.example.playlistmaker.favourite.domain

import com.example.playlistmaker.player.domain.models.PlayerTrack
import kotlinx.coroutines.flow.Flow


interface FavouriteInteractor {
    suspend fun addPlayerTrackToFavourite(playerTrack: PlayerTrack, insertionTimeStamp: Long)
    suspend fun deletePlayerTrackFromFavourite(playerTrack: PlayerTrack)
    suspend fun getTracksIdFromFavourite(): Flow<List<Int>>
}