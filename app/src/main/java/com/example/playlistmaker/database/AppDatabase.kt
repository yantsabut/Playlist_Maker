package com.example.playlistmaker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.new_playlist.data.dao.PlaylistDao
import com.example.playlistmaker.new_playlist.data.dao.PlaylistTrackDao
import com.example.playlistmaker.favourite.data.dao.TrackDao
import com.example.playlistmaker.new_playlist.data.entity.PlaylistEntity
import com.example.playlistmaker.new_playlist.data.entity.PlaylistTrackEntity
import com.example.playlistmaker.favourite.data.entity.TrackEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity:: class, PlaylistTrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao() : PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
}