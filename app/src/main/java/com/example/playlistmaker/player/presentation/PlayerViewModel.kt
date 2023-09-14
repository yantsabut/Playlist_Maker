package com.example.playlistmaker.player.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.favourite.domain.FavouriteInteractor
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.models.PlayerTrack
import com.example.playlistmaker.player.presentation.state_clases.FavouriteTrackState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val STATE_DEFAULT = 0
const val STATE_PREPARED = 1
const val STATE_PLAYING = 2
const val STATE_PAUSED = 3

class PlayerViewModel(
    private val playerTrack: PlayerTrack,
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val audioPlayerDatabaseInteractor: FavouriteInteractor

): ViewModel() {

    private var isFavourite = false

    private var timerJob: Job? = null

    private val _playerTrack = MutableLiveData<PlayerTrack>()
    val playerTrackForRender: LiveData<PlayerTrack> = _playerTrack

    private val _favouriteTrack = MutableLiveData<FavouriteTrackState>()
    val favouriteTrack: LiveData<FavouriteTrackState> = _favouriteTrack

    init {
        preparePlayer()
        assignValToPlayerTrackForRender()
    }

    private val _isCompleted = MutableLiveData(false)
    val isCompleted: LiveData<Boolean> = _isCompleted

    private val _playerState = MutableLiveData(STATE_DEFAULT)

    val playerState: LiveData<Int> = _playerState
    private val _formattedTime = MutableLiveData("00:00")

    val formattedTime: LiveData<String> = _formattedTime

    private fun assignValToPlayerTrackForRender() {
        val playerTrackTo = playerTrack.copy(
            artworkUrl = playerTrack.artworkUrl?.replaceAfterLast('/', "512x512bb.jpg"),
            releaseDate = playerTrack.releaseDate?.split("-", limit = 2)?.get(0),
            trackTime = playerTrack.trackTime?.let { getTimeFormat(it.toLong()) }
        )

        _playerTrack.postValue(playerTrackTo)
    }

    private fun play() {
        audioPlayerInteractor.play()
        _playerState.postValue(STATE_PLAYING)
        startTimer()
        _isCompleted.postValue(false)
    }

    fun pause() {
        audioPlayerInteractor.pause()
        _playerState.postValue(STATE_PAUSED)
       timerJob?.cancel()
    }

    fun release() {
        audioPlayerInteractor.release()
        timerJob?.cancel()
    }

    fun playbackControl() {
        when (_playerState.value) {
            STATE_PLAYING -> pause()
            STATE_PAUSED, STATE_PREPARED -> play()
        }
    }

    private fun preparePlayer() {
        audioPlayerInteractor.prepare(
            callbackPrep = {
                _playerState.postValue(STATE_PREPARED)
            },
            callbackComp = {
                _playerState.postValue(STATE_PREPARED)
                timerJob?.cancel()
                _isCompleted.postValue(true)
            }
        )
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.isPlaying()) {
                delay(UPDATE_TIME_INFO_MS)
                _formattedTime.postValue(getTimeFormat(audioPlayerInteractor.getCurrentPos().toLong()))
            }
        }
    }

    private fun getTimeFormat(value: Long): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(value)

    fun checkEmptinessOrNull(text: String?): String {
        return if (!text.isNullOrEmpty()) text else "n/a"
    }

    fun checkTrackIsFavourite() {

        _favouriteTrack.postValue(
            FavouriteTrackState(
                isFavourite = null,
                isLoading = true
            )
        )

        viewModelScope.launch {
            audioPlayerDatabaseInteractor
                .getTracksIdFromFavourite()
                .collect { listOfIds ->
                    val trackIsFavourite = checkTrackId(listOfIds)

                    assignValueToIsFavourite(trackIsFavourite)
                    _favouriteTrack.postValue(
                        FavouriteTrackState(
                            isFavourite = trackIsFavourite,
                            isLoading = false
                        )
                    )
                }
        }
    }

    suspend fun deletePlayerTrackFromFavourites() {
        audioPlayerDatabaseInteractor.deletePlayerTrackFromFavourite(playerTrack)
    }

    suspend fun addPlayerTrackToFavourites() {
        val insertionTimestamp = System.currentTimeMillis()
        audioPlayerDatabaseInteractor.addPlayerTrackToFavourite(playerTrack, insertionTimestamp)
    }

    private fun checkTrackId(listOfIds: List<Int>): Boolean {
        return listOfIds.contains(playerTrack.trackId)
    }

    fun assignValueToIsFavourite(value: Boolean) {
        isFavourite = value
    }

    fun checkValueFromIsFavourite(): Boolean = isFavourite

    companion object {
        private const val UPDATE_TIME_INFO_MS = 300L
    }
}