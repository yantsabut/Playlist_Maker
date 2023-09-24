package com.example.playlistmaker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.database.dao.PlaylistDao
import com.example.playlistmaker.database.entity.PlaylistEntity

@Database(version = 1, entities = [PlaylistEntity:: class])
abstract class PlaylistDatabase : RoomDatabase() {

    abstract fun playlistDao() : PlaylistDao
}