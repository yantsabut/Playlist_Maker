package com.example.playlistmaker.medialibrary.data.repository

import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.medialibrary.domain.db.PlaylistMediaDatabaseRepository
import com.example.playlistmaker.new_playlist.domain.models.Playlist
import com.example.playlistmaker.new_playlist.domain.models.mapToPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistMediaDatabaseRepositoryImpl (private val playlistDatabase: AppDatabase) :
    PlaylistMediaDatabaseRepository {

    override suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>> = flow {
        val playlistEntityList = playlistDatabase.playlistDao().getPlaylists()
        emit(playlistEntityList.map { playlistEntity -> playlistEntity.mapToPlaylist() })

    }
}