package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TrackAdapter(
    private val onLongClickListener: ((Track) -> Boolean) = {true},
    private val getArtWorkUrl60: Boolean = false,
    private val clickListener: TrackClickListener

): RecyclerView.Adapter<TrackAdapter.TrackHolder>() {

    var tracks = ArrayList<Track>()

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    class TrackHolder(parent: ViewGroup, private val getArtWorkUrl60: Boolean): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
    ) {
        private var artwork = itemView.findViewById<ImageView>(R.id.artwork)
        private var artistName = itemView.findViewById<TextView>(R.id.artistName)
        private var trackName = itemView.findViewById<TextView>(R.id.trackName)
        private var trackTime = itemView.findViewById<TextView>(R.id.trackTime)

        fun bind(track: Track) {

            val formattedTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime?.toLong())

            val artworkUrl = if (getArtWorkUrl60) track.artworkUrl60 else track.artworkUrl

            Glide.with(itemView).load(artworkUrl).placeholder(R.drawable.placeholder).into(artwork)
            artistName.text = track.artistName
            trackName.text = track.trackName
            trackTime.text = formattedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackHolder(parent, getArtWorkUrl60)

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {clickListener.onTrackClick(tracks[position])}
        holder.itemView.setOnLongClickListener { onLongClickListener.invoke(tracks[position]) }
    }

    override fun getItemCount() = tracks.size

}
