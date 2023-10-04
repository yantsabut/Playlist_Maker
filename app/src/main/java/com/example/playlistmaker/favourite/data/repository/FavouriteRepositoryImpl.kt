package com.example.playlistmaker.favourite.data.repository

import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.favourite.data.repository.FavouriteRepository
import com.example.playlistmaker.player.data.converters.PlayerTrackDbConverter
import com.example.playlistmaker.player.data.dto.PlayerTrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playerTrackDbConverter: PlayerTrackDbConverter
) : FavouriteRepository {

    override suspend fun addPlayerTrackToFavourite(playerTrackDto: PlayerTrackDto) {
        appDatabase.trackDao().insertTrack(playerTrackDbConverter.map(playerTrackDto))
    }

    override suspend fun deletePlayerTrackFromFavourite(playerTrackDto: PlayerTrackDto) {
        appDatabase.trackDao().deleteTrack(playerTrackDbConverter.map(playerTrackDto))
    }

    override suspend fun getTracksIdFromFavourite(): Flow<List<Int>> = flow {
        emit(appDatabase.trackDao().getTracksId())
    }

}