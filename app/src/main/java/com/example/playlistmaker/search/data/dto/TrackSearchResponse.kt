package com.example.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName


class TrackSearchResponse(@SerializedName("results") val tracks: ArrayList<TrackDto>): Response()