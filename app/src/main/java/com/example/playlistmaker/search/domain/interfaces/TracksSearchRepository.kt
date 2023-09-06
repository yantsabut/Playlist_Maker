package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Response
import kotlinx.coroutines.flow.Flow


interface TracksSearchRepository {
    fun searchTracks(expression: String): Flow<Response<List<Track>>>
}