package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment: Fragment() {

private lateinit var binding: FragmentSettingsBinding

    private lateinit var shareAppFrameLayout: FrameLayout
    private lateinit var supportFrameLayout: FrameLayout
    private lateinit var agreementFrameLayout: FrameLayout
    private lateinit var themeSwitcher: SwitchMaterial

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareAppFrameLayout = binding.shareAppFrameLayout
        supportFrameLayout = binding.supportFrameLayout
        agreementFrameLayout = binding.agreementFrameLayout
        themeSwitcher = binding.themeSwitcher

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

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.saveAndChangeThemeState(checked)
        }
    }
}