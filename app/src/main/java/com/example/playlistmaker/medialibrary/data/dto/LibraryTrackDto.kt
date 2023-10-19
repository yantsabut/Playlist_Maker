package com.example.playlistmaker.medialibrary.data.dto

data class LibraryTrackDto(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTime: String?,
    val artworkUrl: String?,
    val artworkUrl60: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val insertTimeStamp: Long? = null
)
