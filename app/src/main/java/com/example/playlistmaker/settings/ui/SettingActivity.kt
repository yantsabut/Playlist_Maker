package com.example.playlistmaker.settings.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.example.playlistmaker.settings.presentation.SettingsViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingActivity : AppCompatActivity() {

    private lateinit var backArrowImageView: ImageView
    private lateinit var shareAppFrameLayout: FrameLayout
    private lateinit var supportFrameLayout: FrameLayout
    private lateinit var agreementFrameLayout: FrameLayout
    private lateinit var themeSwitcher: SwitchMaterial

    private lateinit var viewModel: SettingsViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        viewModel =
            ViewModelProvider(this, SettingsViewModelFactory(this))[SettingsViewModel::class.java]

        shareAppFrameLayout = findViewById(R.id.share_app)
        supportFrameLayout = findViewById(R.id.write_in_support)
        agreementFrameLayout = findViewById(R.id.user_agreement)
        backArrowImageView = findViewById(R.id.backArrowImageView)

        themeSwitcher.isChecked = viewModel.getThemeState()

        shareAppFrameLayout.setOnClickListener {
            val message = viewModel.getLinkToCourse()

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        supportFrameLayout.setOnClickListener {
            val message = viewModel.getEmailMessage()
            val subject = viewModel.getEmailSubject()
            val mailArray = viewModel.getArrayOfEmailAddresses()

            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, mailArray)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        agreementFrameLayout.setOnClickListener {
            val url = viewModel.getPracticumOffer()

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }

        backArrowImageView.setOnClickListener {
            finish()
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.saveAndChangeThemeState(checked)
        }
    }
}