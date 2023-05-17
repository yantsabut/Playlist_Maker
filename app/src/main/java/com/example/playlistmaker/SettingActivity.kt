package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)

        val buttonShare = findViewById<TextView>(R.id.share_app)
        val buttonSupport = findViewById<TextView>(R.id.write_in_support)
        val buttonAgreement = findViewById<TextView>(R.id.user_agreement)
        val buttonSettingsBack = findViewById<ImageView>(R.id.backArrowImageView)

        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.developer))
            shareIntent.type = "*/*"
            startActivity(shareIntent)
        }

        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SEND)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            startActivity(supportIntent)
        }

        buttonAgreement.setOnClickListener {
            val agreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer)))
            startActivity(agreementIntent)
        }

        buttonSettingsBack.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        if ((applicationContext as App).darkTheme) {
            themeSwitcher.setChecked(true);
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPreferences.edit()
                .putBoolean(KEY_FOR_APP_THEME, checked)
                .apply()
        }
    }
}