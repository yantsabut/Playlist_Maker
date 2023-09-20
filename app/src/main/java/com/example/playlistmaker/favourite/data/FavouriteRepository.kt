package com.example.playlistmaker.favourite.data

import com.example.playlistmaker.player.data.dto.PlayerTrackDto
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    suspend fun addPlayerTrackToFavourite(playerTrackDto: PlayerTrackDto)
    suspend fun deletePlayerTrackFromFavourite(playerTrackDto: PlayerTrackDto)
    suspend fun getTracksIdFromFavourite(): Flow<List<Int>>
}