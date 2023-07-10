package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.interfaces.TracksSearchRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Response


class TracksSearchSearchRepositoryImpl(private val networkClient: NetworkClient):
    TracksSearchRepository {

    override fun searchTracks(expression: String): Response<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Response.Error(isFailed = false)
            }
            200 -> {
                Response.Success((response as TrackSearchResponse).tracks.map {
                    Track(
                        trackId = it.trackId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTime = it.trackTime,
                        artworkUrl = it.artworkUrl,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )})
            }
            else -> {
                Response.Error(isFailed = true)
            }
        }
    }
}