package com.example.playlistmaker


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory {
    companion object {

        var historyTrackList = ArrayList<Track>()
        const val HISTORY_TRACK_LIST = "key_for_history_track_list"
        const val MAX_HISTORY_SIZE = 10

        fun fill() {
            val json = App.applicationPrefs.getString(HISTORY_TRACK_LIST, null)
            if (!json.isNullOrEmpty()) {
                val sType = object : TypeToken<ArrayList<Track>>() {}.type
                historyTrackList = Gson().fromJson<ArrayList<Track>>(json, sType)
            }
        }

        fun add(track: Track) {
            for (t in historyTrackList) {
                if (t.trackId == track.trackId) {
                    historyTrackList.remove(t)
                }
            }
            if(historyTrackList.size >= MAX_HISTORY_SIZE) {
                historyTrackList.removeAt(historyTrackList.size - 1)
            }

            historyTrackList.add(0, track)

            wright()
        }

        private fun wright() {
            val json = Gson().toJson(historyTrackList)
            App.applicationPrefs.edit()
                .putString(HISTORY_TRACK_LIST, json)
                .apply()
        }

        fun clear() {
            historyTrackList.clear()
            wright()
        }
    }
}