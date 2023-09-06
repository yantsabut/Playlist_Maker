package com.example.playlistmaker.medialibrary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackToTrackConverter
import com.example.playlistmaker.medialibrary.domain.db.LibraryDatabaseInteractor
import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import com.example.playlistmaker.medialibrary.presentation.state_clases.LibraryTracksState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class MedialibraryFavouritesViewModel (
    private val libraryDatabaseInteractor: LibraryDatabaseInteractor,
    private val libraryTrackToTrackConverter: LibraryTrackToTrackConverter
) : ViewModel() {

    private val _databaseTracksState = MutableLiveData<LibraryTracksState>()
    val databaseTracksState: LiveData<LibraryTracksState> = _databaseTracksState

    fun convertLibraryTrackToTrack(libraryTrack: LibraryTrack): Track {
        return libraryTrackToTrackConverter.map(libraryTrack)
    }

    fun fillData() {

        _databaseTracksState.postValue(
            LibraryTracksState(
                libraryTracks = emptyList(),
                isLoading = true
            )
        )

        viewModelScope.launch {
            libraryDatabaseInteractor
                .getPlayerTracksFromDatabase()
                .collect { libraryTracks ->
                    processResult(libraryTracks)
                }
        }

    }

    private fun processResult(libraryTracks: List<LibraryTrack>) {

        if (libraryTracks.isEmpty()) {
            _databaseTracksState.postValue(
                LibraryTracksState(
                    libraryTracks = emptyList(),
                    isLoading = false
                )
            )
        } else {
            _databaseTracksState.postValue(
                LibraryTracksState(
                    libraryTracks = libraryTracks,
                    isLoading = false
                )
            )
        }
    }

}