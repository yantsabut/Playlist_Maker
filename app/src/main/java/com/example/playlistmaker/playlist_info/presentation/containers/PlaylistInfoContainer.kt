package com.example.playlistmaker.playlist_info.presentation.containers

import com.example.playlistmaker.search.domain.models.Track

data class PlaylistInfoContainer(
    val totalTime: String,
    val playlistTracks: List<Track>
)