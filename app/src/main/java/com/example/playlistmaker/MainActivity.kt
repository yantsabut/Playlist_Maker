package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import java.util.Objects

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<LinearLayout>(R.id.search_button) as Button
        val imageClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали кнопку Поиск!", Toast.LENGTH_SHORT).show()

            }
        }
        buttonSearch.setOnClickListener (imageClickListener)

        val buttonLibrary = findViewById<ImageView>(R.id.library_button) as Button
        buttonLibrary.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали кнопку Медиатека!", Toast.LENGTH_SHORT).show()
        }
        val buttonSetting = findViewById<ImageView>(R.id.setting_button) as Button
        buttonSetting.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали кнопку Настройки!", Toast.LENGTH_SHORT).show()
        }
    }
}