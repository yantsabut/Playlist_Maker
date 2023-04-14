package com.example.playlistmaker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String
)

