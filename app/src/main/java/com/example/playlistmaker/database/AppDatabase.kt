package com.example.playlistmaker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.database.dao.PlaylistDao
import com.example.playlistmaker.database.dao.PlaylistTrackDao
import com.example.playlistmaker.database.dao.TrackDao
import com.example.playlistmaker.database.entity.PlaylistEntity
import com.example.playlistmaker.database.entity.PlaylistTrackEntity
import com.example.playlistmaker.database.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity:: class, PlaylistTrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao() : PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
}