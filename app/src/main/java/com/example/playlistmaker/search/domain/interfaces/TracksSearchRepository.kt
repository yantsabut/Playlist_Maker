package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Response


interface TracksSearchRepository {
    fun searchTracks(expression: String): Response<List<Track>>
}