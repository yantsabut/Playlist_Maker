package com.example.playlistmaker.medialibrary.domain.converters

import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import com.example.playlistmaker.search.domain.models.Track

class LibraryTrackToTrackConverter {

    fun map(libraryTrack: LibraryTrack): Track {
        return Track(
            trackId = libraryTrack.trackId,
            trackName = libraryTrack.trackName,
            artistName = libraryTrack.artistName,
            trackTime = libraryTrack.trackTime,
            artworkUrl = libraryTrack.artworkUrl,
            collectionName = libraryTrack.collectionName,
            releaseDate = libraryTrack.releaseDate,
            primaryGenreName = libraryTrack.primaryGenreName,
            country = libraryTrack.country,
            previewUrl = libraryTrack.previewUrl
        )
    }

}