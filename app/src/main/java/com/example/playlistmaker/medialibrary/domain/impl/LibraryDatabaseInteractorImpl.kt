package com.example.playlistmaker.medialibrary.domain.impl

import com.example.playlistmaker.medialibrary.data.dto.LibraryTrackDto
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackDataConverter
import com.example.playlistmaker.medialibrary.domain.db.LibraryInteractor
import com.example.playlistmaker.medialibrary.domain.db.LibraryRepository
import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LibraryDatabaseInteractorImpl(
    private val libraryDatabaseRepository: LibraryRepository,
    private val libraryTrackDataConverter: LibraryTrackDataConverter
) : LibraryInteractor {

    override suspend fun getPlayerTracksFromDatabase(): Flow<List<LibraryTrack>> {
        return libraryDatabaseRepository.getPlayerTracksFromDatabase().map { list ->
            convertListLibraryTrackDtoToListLibraryTrack(list)
        }
    }

    private fun convertListLibraryTrackDtoToListLibraryTrack(libraryTrackDtoList: List<LibraryTrackDto>): List<LibraryTrack> {
        return libraryTrackDtoList.map { track -> libraryTrackDataConverter.map(track) }
    }
}