package com.example.playlistmaker.new_playlist.data.dao

import androidx.room.*
import com.example.playlistmaker.new_playlist.data.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("DELETE FROM playlist_table")
    suspend fun clearTable()

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

}