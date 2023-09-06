package com.example.playlistmaker.player.data.converters

import com.example.playlistmaker.database.entity.TrackEntity
import com.example.playlistmaker.player.data.dto.PlayerTrackDto

class PlayerTrackDbConverter {
    fun map(playerTrackDto: PlayerTrackDto): TrackEntity {
        return TrackEntity(
            trackId = playerTrackDto.trackId,
            trackName = playerTrackDto.trackName,
            artistName = playerTrackDto.artistName,
            trackTime = playerTrackDto.trackTime,
            artworkUrl = playerTrackDto.artworkUrl,
            collectionName = playerTrackDto.collectionName,
            releaseDate = playerTrackDto.releaseDate,
            primaryGenreName = playerTrackDto.primaryGenreName,
            country = playerTrackDto.country,
            previewUrl = playerTrackDto.previewUrl,
            insertTimeStamp = playerTrackDto.insertionTimeStamp
        )
    }

    fun map(trackEntity: TrackEntity): PlayerTrackDto {
        return PlayerTrackDto(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTime = trackEntity.trackTime,
            artworkUrl = trackEntity.artworkUrl,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            previewUrl = trackEntity.previewUrl
        )
    }
}