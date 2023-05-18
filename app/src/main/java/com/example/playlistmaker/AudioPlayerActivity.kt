package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        backArrow = findViewById(R.id.backArrowPlaylist)
        coverImage = findViewById(R.id.coverMax)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        duration = findViewById(R.id.durationName)
        collectionName = findViewById(R.id.albumName)
        year = findViewById(R.id.yearName)
        genre = findViewById(R.id.genreName)
        country = findViewById(R.id.countryName)

        backArrow.setOnClickListener {
            finish()
            val value: String? = intent.getStringExtra(KEY_FOR_PLAYLIST)
            val track: Track? = Gson().fromJson(value, Track::class.java)

            if (track != null) {

                val formattedTime =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime.toLong())
                val artworkHiResolution = track.artworkUrl.replaceAfterLast('/', "512x512bb.jpg")

                Glide.with(this)
                    .load(artworkHiResolution)
                    .placeholder(R.drawable.track_placeholder_max)
                    .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius)))
                    .into(coverImage)

                trackName.text = track.trackName
                artistName.text = track.artistName
                duration.text = formattedTime

                if (track.collectionName != null && track.collectionName.isNotEmpty()) {
                    collectionName.text = track.collectionName
                } else {
                    collectionName.text = "n/a"
                }

                year.text = track.releaseDate.split("-", limit = 2)[0]
                genre.text = track.primaryGenreName
                country.text = track.country
            }
        }
    }
}