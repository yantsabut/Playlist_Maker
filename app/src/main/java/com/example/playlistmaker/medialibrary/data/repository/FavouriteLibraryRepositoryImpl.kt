package com.example.playlistmaker.medialibrary.data.repository

import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.favourite.data.entity.TrackEntity
import com.example.playlistmaker.medialibrary.data.converters.TrackDbConverter
import com.example.playlistmaker.medialibrary.data.dto.LibraryTrackDto
import com.example.playlistmaker.favourite.data.repository.FavouriteLibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteLibraryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavouriteLibraryRepository {

    override suspend fun getPlayerTracksFromFavourite(): Flow<List<LibraryTrackDto>> = flow {
        val trackEntityList: List<TrackEntity> = appDatabase.trackDao().getTracks()
        emit(trackEntityList.map { trackEntity -> trackDbConverter.map(trackEntity) })
    }

}