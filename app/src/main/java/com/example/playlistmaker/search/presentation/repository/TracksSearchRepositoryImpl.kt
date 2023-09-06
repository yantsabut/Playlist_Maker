package com.example.playlistmaker.search.presentation.repository

import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.interfaces.TracksSearchRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksSearchSearchRepositoryImpl(private val networkClient: NetworkClient): TracksSearchRepository {

    override fun searchTracks(expression: String): Flow<Response<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when(response.resultCode) {

            -1 -> {
                emit(Response.Error(isFailed = false))
            }

            200 -> {
                emit(Response.Success((response as TrackSearchResponse).tracks.map {
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
                    )}))
            }

            else -> {
                emit(Response.Error(isFailed = true))
            }
        }
    }
}