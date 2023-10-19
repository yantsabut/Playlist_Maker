package com.example.playlistmaker.new_playlist.data.dao

import androidx.room.*
import com.example.playlistmaker.new_playlist.data.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table WHERE trackId IN (:ids) ORDER BY insertTimeStamp DESC")
    suspend fun getTracksByListIds(ids: List<Int>): List<PlaylistTrackEntity>

    @Delete
    suspend fun deletePlaylistTrack(track: PlaylistTrackEntity)

    @Query("DELETE FROM playlist_track_table WHERE trackId =:id")
    suspend fun deleteTrackById(id: Int)

}