package com.example.playlistmaker.favourite.data.repository

import com.example.playlistmaker.medialibrary.data.dto.LibraryTrackDto
import kotlinx.coroutines.flow.Flow


interface FavouriteLibraryRepository {
    suspend fun getPlayerTracksFromFavourite(): Flow<List<LibraryTrackDto>>
}