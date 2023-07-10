package com.example.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.medialibrary.ui.LibraryActivity
import com.example.playlistmaker.search.ui.SearchActivity
import com.example.playlistmaker.settings.ui.SettingActivity


class MainActivity : AppCompatActivity() {

    private lateinit var buttonSearch: Button
    private lateinit var buttonLibrary: Button
    private lateinit var buttonSetting: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSearch = findViewById(R.id.search)

        buttonLibrary = findViewById(R.id.library)

        buttonSetting = findViewById(R.id.settings)


        val searchButtonClickListener: View.OnClickListener = View.OnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
        buttonSearch.setOnClickListener(searchButtonClickListener)

        buttonLibrary.setOnClickListener {
            val intent = Intent(this@MainActivity, LibraryActivity::class.java)
            startActivity(intent)
        }

        buttonSetting.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
        }
    }
}