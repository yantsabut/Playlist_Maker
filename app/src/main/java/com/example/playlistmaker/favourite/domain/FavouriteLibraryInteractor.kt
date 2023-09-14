package com.example.playlistmaker.favourite.domain

import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import kotlinx.coroutines.flow.Flow


interface FavouriteLibraryInteractor {
    suspend fun getPlayerTracksFromFavourite(): Flow<List<LibraryTrack>>
}