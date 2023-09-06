package com.example.playlistmaker.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.database.dao.TrackDao
import com.example.playlistmaker.database.entity.TrackEntity

@Database(version = 2, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}