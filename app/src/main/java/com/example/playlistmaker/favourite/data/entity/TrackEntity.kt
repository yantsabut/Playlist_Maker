package com.example.playlistmaker.favourite.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
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