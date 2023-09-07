package com.example.playlistmaker.medialibrary.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.domain.models.LibraryTrack
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LibraryTrackAdapter(
    val clickListener: TrackClickListener): RecyclerView.Adapter<LibraryTrackAdapter.TrackHolder>() {

    var tracks = ArrayList<LibraryTrack>()

    fun interface TrackClickListener {
        fun onTrackClick(libraryTrack: LibraryTrack)
    }

    class TrackHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
    ) {
        private var artwork = itemView.findViewById<ImageView>(R.id.artwork)
        private var artistName = itemView.findViewById<TextView>(R.id.artistName)
        private var trackName = itemView.findViewById<TextView>(R.id.trackName)
        private var trackTime = itemView.findViewById<TextView>(R.id.trackTime)

        fun bind(libraryTrack: LibraryTrack) {

            val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(libraryTrack.trackTime?.toLong())

            Glide.with(itemView)
                .load(libraryTrack.artworkUrl)
                .placeholder(R.drawable.placeholder)
                .into(artwork)
            artistName.text = libraryTrack.artistName
            trackName.text = libraryTrack.trackName
            trackTime.text = formattedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TrackHolder(parent)

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {clickListener.onTrackClick(tracks[position])}
    }

    override fun getItemCount() = tracks.size

}