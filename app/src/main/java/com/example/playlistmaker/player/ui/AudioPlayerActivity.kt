package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.KEY_FOR_PLAYER
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.models.PlayerTrack
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.player.presentation.STATE_PAUSED
import com.example.playlistmaker.player.presentation.STATE_PLAYING
import com.example.playlistmaker.player.presentation.state_clases.FavouriteTrackState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.Serializable

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var backArrow: ImageView
    private lateinit var coverImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var collectionName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var playButton: ImageView
    private lateinit var durationInTime: TextView
    private lateinit var favouriteButton: ImageButton

    private lateinit var playerTrack: PlayerTrack

    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(playerTrack)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_audio_player)

        backArrow = findViewById(R.id.backArrowPlaylist)
        coverImage = findViewById(R.id.coverMax)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        duration = findViewById(R.id.durationName)
        collectionName = findViewById(R.id.albumName)
        year = findViewById(R.id.yearName)
        genre = findViewById(R.id.genreName)
        country = findViewById(R.id.countryName)
        playButton = findViewById(R.id.playButton)
        durationInTime = findViewById(R.id.durationInTime)
        favouriteButton = findViewById(R.id.favouriteButton)

        backArrow.setOnClickListener {
            finish()
        }

        playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        favouriteButton.setOnClickListener {
            if (viewModel.checkValueFromIsFavourite()) {
                favouriteButton.setImageResource(R.drawable.like_button)
                viewModel.assignValueToIsFavourite(false)
                lifecycleScope.launch {
                    viewModel.deletePlayerTrackFromFavourites()
                }
            } else {
                favouriteButton.setImageResource(R.drawable.like_button_marked)
                viewModel.assignValueToIsFavourite(true)
                lifecycleScope.launch {
                    viewModel.addPlayerTrackToFavourites()
                }
            }
        }

        val track = intent.getSerializable(KEY_FOR_PLAYER, Track::class.java)

        playerTrack = convertTrackToPlayerTrack(track)

        if (playerTrack.previewUrl == null) {
            playButton.isEnabled = false
        }

        viewModel.checkTrackIsFavourite()

        viewModel.playerTrackForRender.observe(this) { playerTrack ->
            render(playerTrack)
        }

        viewModel.playerState.observe(this) { statePlaying ->

            when (statePlaying) {
                STATE_PLAYING -> playButton.setImageResource(R.drawable.pause_button)
                STATE_PAUSED -> playButton.setImageResource(R.drawable.play_button)
            }
        }

        viewModel.isCompleted.observe(this) { isCompleted ->
            if (isCompleted) {
                durationInTime.text = getString(R.string.player_start_position)
                playButton.setImageResource(R.drawable.play_button)
            }
        }

        viewModel.formattedTime.observe(this) { trackTime ->
            durationInTime.text = trackTime
        }

        viewModel.favouriteTrack.observe(this) { favouriteTrackState ->
            renderFavouriteButton(favouriteTrackState)
        }

    }

    private fun renderFavouriteButton(favouriteTrackState: FavouriteTrackState) {

        when {

            favouriteTrackState.isLoading -> {
                favouriteButton.isEnabled = false
            }

            else -> {
                favouriteButton.isEnabled = true

                if (favouriteTrackState.isFavourite == true) {
                    favouriteButton.setImageResource(R.drawable.like_button_marked)
                } else {
                    favouriteButton.setImageResource(R.drawable.like_button)
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        viewModel.release()
        super.onDestroy()

    }

    fun <T : Serializable?> Intent.getSerializable(key: String, m_class: Class<T>): T {
        return if (SDK_INT >= TIRAMISU)
            this.getSerializableExtra(key, m_class)!!
        else
            this.getSerializableExtra(key) as T
    }

    private fun convertTrackToPlayerTrack(track: Track): PlayerTrack {
        return PlayerTrack(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl = track.artworkUrl,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            insertionTimeStamp = null
        )
    }

    private fun render(track: PlayerTrack) {

        trackName.text = viewModel.checkEmptinessOrNull(track.trackName)
        artistName.text = viewModel.checkEmptinessOrNull(track.artistName)
        duration.text = viewModel.checkEmptinessOrNull(track.trackTime)
        collectionName.text = viewModel.checkEmptinessOrNull(track.collectionName)
        year.text = viewModel.checkEmptinessOrNull(track.releaseDate)
        genre.text = viewModel.checkEmptinessOrNull(track.primaryGenreName)
        country.text = viewModel.checkEmptinessOrNull(track.country)

        Glide.with(this)
            .load(track.artworkUrl)
            .placeholder(R.drawable.track_placeholder_max)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius)))
            .into(coverImage)
    }
}
