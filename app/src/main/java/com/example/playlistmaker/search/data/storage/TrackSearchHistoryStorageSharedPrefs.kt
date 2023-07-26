package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.KEY_FOR_HISTORY_LIST
import com.example.playlistmaker.search.data.dto.TrackDto
import com.google.gson.Gson



class TrackSearchHistoryStorageSharedPrefs(private val sharedPreferences: SharedPreferences?): TrackSearchHistoryStorage {

    override fun getTracksFromStorage(): Array<TrackDto> {
        val json = sharedPreferences?.getString(KEY_FOR_HISTORY_LIST, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<TrackDto>::class.java)
    }

    override fun saveTracksToStorage(tracks: ArrayList<TrackDto>) {
        val json = Gson().toJson(tracks)
        sharedPreferences?.edit()
            ?.putString(KEY_FOR_HISTORY_LIST, json)
            ?.apply()
    }
}