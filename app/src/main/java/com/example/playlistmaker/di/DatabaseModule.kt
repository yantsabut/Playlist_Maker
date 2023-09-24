package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.database.PlaylistDatabase
import com.example.playlistmaker.database.PlaylistTrackDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<PlaylistDatabase> {
       Room.databaseBuilder(androidContext(), PlaylistDatabase::class.java, "playlist_database.db")
           .fallbackToDestructiveMigration()
           .build()
    }

    single<PlaylistTrackDatabase> {
           Room.databaseBuilder(androidContext(), PlaylistTrackDatabase::class.java, "playlist_track_databases.db")
               .fallbackToDestructiveMigration()
               .build()
    }

}