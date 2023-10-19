package com.example.playlistmaker.medialibrary.data.converters

import com.example.playlistmaker.favourite.data.entity.TrackEntity
import com.example.playlistmaker.medialibrary.data.dto.LibraryTrackDto

class TrackDbConverter {
    fun map(libraryTrackDto: LibraryTrackDto): TrackEntity {
        return TrackEntity(
            trackId = libraryTrackDto.trackId,
            trackName = libraryTrackDto.trackName,
            artistName = libraryTrackDto.artistName,
            trackTime = libraryTrackDto.trackTime,
            artworkUrl = libraryTrackDto.artworkUrl,
            artworkUrl60 = libraryTrackDto.artworkUrl60,
            collectionName = libraryTrackDto.collectionName,
            releaseDate = libraryTrackDto.releaseDate,
            primaryGenreName = libraryTrackDto.primaryGenreName,
            country = libraryTrackDto.country,
            previewUrl = libraryTrackDto.previewUrl,
            insertTimeStamp = libraryTrackDto.insertTimeStamp
        )
    }

    fun map(trackEntity: TrackEntity): LibraryTrackDto {
        return LibraryTrackDto(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTime = trackEntity.trackTime,
            artworkUrl = trackEntity.artworkUrl,
            artworkUrl60 = trackEntity.artworkUrl60,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            previewUrl = trackEntity.previewUrl,
            insertTimeStamp = trackEntity.insertTimeStamp
        )
    }
}