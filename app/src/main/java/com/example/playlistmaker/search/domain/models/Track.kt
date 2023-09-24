package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.database.entity.PlaylistTrackEntity
import com.example.playlistmaker.player.domain.models.PlayerTrack
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: String?,
    @SerializedName("artworkUrl100") val artworkUrl: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
): Serializable

fun Track.mapToPlaylistTrackEntity(): PlaylistTrackEntity {
    return PlaylistTrackEntity(
        trackId,
        trackName,
        artistName,
        trackTime,
        artworkUrl,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl,
        System.currentTimeMillis()
    )
}

fun Track.mapToPlayerTrack(): PlayerTrack {
    return PlayerTrack(trackId, trackName, artistName, trackTime, artworkUrl, collectionName, releaseDate, primaryGenreName, country, previewUrl, null)
}