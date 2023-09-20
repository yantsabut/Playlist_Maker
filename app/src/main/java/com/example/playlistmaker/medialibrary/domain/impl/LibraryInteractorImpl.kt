package com.example.playlistmaker.medialibrary.domain.impl

import com.example.playlistmaker.medialibrary.data.dto.LibraryTrackDto
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackDataConverter
import com.example.playlistmaker.favourite.domain.FavouriteLibraryInteractor
import com.example.playlistmaker.favourite.data.FavouriteLibraryRepository
import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LibraryInteractorImpl(
    private val libraryDatabaseRepository: FavouriteLibraryRepository,
    private val libraryTrackDataConverter: LibraryTrackDataConverter
) : FavouriteLibraryInteractor {

    override suspend fun getPlayerTracksFromFavourite(): Flow<List<LibraryTrack>> {
        return libraryDatabaseRepository.getPlayerTracksFromFavourite().map { list ->
            convertListLibraryTrackDtoToListLibraryTrack(list)
        }
    }

    private fun convertListLibraryTrackDtoToListLibraryTrack(libraryTrackDtoList: List<LibraryTrackDto>): List<LibraryTrack> {
        return libraryTrackDtoList.map { track -> libraryTrackDataConverter.map(track) }
    }
}