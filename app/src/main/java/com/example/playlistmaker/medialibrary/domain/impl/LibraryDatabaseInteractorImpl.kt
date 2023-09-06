package com.example.playlistmaker.medialibrary.domain.impl

import com.example.playlistmaker.medialibrary.data.dto.LibraryTrackDto
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackDataConverter
import com.example.playlistmaker.medialibrary.domain.db.LibraryDatabaseInteractor
import com.example.playlistmaker.medialibrary.domain.db.LibraryDatabaseRepository
import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LibraryDatabaseInteractorImpl(
    private val libraryDatabaseRepository: LibraryDatabaseRepository,
    private val libraryTrackDataConverter: LibraryTrackDataConverter
) : LibraryDatabaseInteractor {

    override suspend fun getPlayerTracksFromDatabase(): Flow<List<LibraryTrack>> {
        return libraryDatabaseRepository.getPlayerTracksFromDatabase().map { list ->
            convertListLibraryTrackDtoToListLibraryTrack(list)
        }
    }

    private fun convertListLibraryTrackDtoToListLibraryTrack(libraryTrackDtoList: List<LibraryTrackDto>): List<LibraryTrack> {
        return libraryTrackDtoList.map { track -> libraryTrackDataConverter.map(track) }
    }
}