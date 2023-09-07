package com.example.playlistmaker.medialibrary.data.repository

import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.database.entity.TrackEntity
import com.example.playlistmaker.medialibrary.data.converters.TrackDbConverter
import com.example.playlistmaker.medialibrary.data.dto.LibraryTrackDto
import com.example.playlistmaker.medialibrary.domain.db.LibraryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LibraryDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : LibraryRepository {

    override suspend fun getPlayerTracksFromDatabase(): Flow<List<LibraryTrackDto>> = flow {
        val trackEntityList: List<TrackEntity> = appDatabase.trackDao().getTracks()
        emit(trackEntityList.map { trackEntity -> trackDbConverter.map(trackEntity) })
    }

}