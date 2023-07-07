package com.example.playlistmaker.search.domain.interfaces

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource


interface TracksSearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}