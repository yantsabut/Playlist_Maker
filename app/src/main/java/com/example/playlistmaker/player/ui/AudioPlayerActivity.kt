package com.example.playlistmaker.player.ui

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.player.domain.models.PlayerTrack
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.player.presentation.STATE_PAUSED
import com.example.playlistmaker.player.presentation.STATE_PLAYING
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.KEY_FOR_PLAYER
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

    private lateinit var playerTrack: PlayerTrack

    private val viewModel by viewModel<PlayerViewModel> {
        parametersOf(playerTrack)
    }

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

        backArrow.setOnClickListener {
            finish()
        }

        playButton.setOnClickListener {
            viewModel.playbackControl()
        }

        val track = intent.getSerializable(KEY_FOR_PLAYER, Track::class.java)

        playerTrack = convertTrackToPlayerTrack(track)

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
            previewUrl = track.previewUrl
        )
    }

    private fun render(track: PlayerTrack) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text = track.trackTime

        if (!track.collectionName.isNullOrEmpty()) {
            collectionName.text = track.collectionName
        } else {
            collectionName.text = getString(R.string.unknown)
        }

        year.text = track.releaseDate
        genre.text = track.primaryGenreName
        country.text = track.country

        Glide.with(this)
            .load(track.artworkUrl)
            .placeholder(R.drawable.track_placeholder_max)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius)))
            .into(coverImage)
    }
}
