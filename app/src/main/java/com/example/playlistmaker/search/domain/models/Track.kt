package com.example.playlistmaker.search.domain.models

import com.example.playlistmaker.new_playlist.data.entity.PlaylistTrackEntity
import com.example.playlistmaker.player.domain.models.PlayerTrack
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: String?,
    @SerializedName("artworkUrl100") val artworkUrl: String?,
    val artworkUrl60: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val insertTimeStamp: Long? = null
): Serializable

fun Track.mapToPlaylistTrackEntity(newTimeStamp: Boolean = true): PlaylistTrackEntity {

    val timeStamp = if (newTimeStamp) System.currentTimeMillis() else this.insertTimeStamp

    return PlaylistTrackEntity(
        trackId,
        trackName,
        artistName,
        trackTime,
        artworkUrl,
        artworkUrl60,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl,
        insertTimeStamp = timeStamp
    )
}

fun Track.mapToPlayerTrack(): PlayerTrack {
    return PlayerTrack(
        trackId,
        trackName,
        artistName,
        trackTime,
        artworkUrl,
        artworkUrl60,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl,
        null)
}

fun PlaylistTrackEntity.mapToTrack(): Track {
    return Track(
        trackId,
        trackName,
        artistName,
        trackTime,
        artworkUrl,
        artworkUrl60,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl,
        insertTimeStamp
    )
}