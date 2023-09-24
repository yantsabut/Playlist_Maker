package com.example.playlistmaker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.database.dao.PlaylistTrackDao
import com.example.playlistmaker.database.entity.PlaylistTrackEntity

@Database(version = 1, entities = [PlaylistTrackEntity::class])
abstract class PlaylistTrackDatabase() : RoomDatabase() {

    abstract fun playlistTrackDao(): PlaylistTrackDao

}