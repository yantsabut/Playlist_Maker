package com.example.playlistmaker

import com.example.playlistmaker.domain.Track
import com.google.gson.annotations.SerializedName

class TrackResponse(@SerializedName("results") val tracks: ArrayList<Track>)