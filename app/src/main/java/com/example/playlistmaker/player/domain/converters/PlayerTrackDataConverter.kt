package com.example.playlistmaker.player.domain.converters

import com.example.playlistmaker.player.data.dto.PlayerTrackDto
import com.example.playlistmaker.player.domain.models.PlayerTrack

class PlayerTrackDataConverter {
    fun map(playerTrack: PlayerTrack, insertionTimeStamp: Long? = null): PlayerTrackDto {
        return PlayerTrackDto(
            trackId = playerTrack.trackId,
            trackName = playerTrack.trackName,
            artistName = playerTrack.artistName,
            trackTime = playerTrack.trackTime,
            artworkUrl = playerTrack.artworkUrl,
            collectionName = playerTrack.collectionName,
            releaseDate = playerTrack.releaseDate,
            primaryGenreName = playerTrack.primaryGenreName,
            country = playerTrack.country,
            previewUrl = playerTrack.previewUrl,
            insertionTimeStamp = insertionTimeStamp
        )
    }

    fun map(playerTrackDto: PlayerTrackDto): PlayerTrack {
        return PlayerTrack(
            trackId = playerTrackDto.trackId,
            trackName = playerTrackDto.trackName,
            artistName = playerTrackDto.artistName,
            trackTime = playerTrackDto.trackTime,
            artworkUrl = playerTrackDto.artworkUrl,
            collectionName = playerTrackDto.collectionName,
            releaseDate = playerTrackDto.releaseDate,
            primaryGenreName = playerTrackDto.primaryGenreName,
            country = playerTrackDto.country,
            previewUrl = playerTrackDto.previewUrl
        )
    }
}