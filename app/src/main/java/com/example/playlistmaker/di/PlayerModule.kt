package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.converters.PlayerTrackDbConverter
import com.example.playlistmaker.player.data.repository.AudioPlayerDatabaseRepositoryImpl
import com.example.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.converters.PlayerTrackDataConverter
import com.example.playlistmaker.player.domain.interactors.AudioPlayerDatabaseInteractorImpl
import com.example.playlistmaker.player.domain.interactors.AudioPlayerInteractorImpl
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerDatabaseInteractor
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerDatabaseRepository
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerRepository
import com.example.playlistmaker.player.domain.models.PlayerTrack
import com.example.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module


    val playerModule = module {
        factory { MediaPlayer() }

        factory <AudioPlayerRepository> {
            AudioPlayerRepositoryImpl(mediaPlayer = get())
        }

        factory<AudioPlayerInteractor> { (playerTrack: PlayerTrack) ->
            AudioPlayerInteractorImpl(playerTrack = playerTrack, audioPlayerRepository = get())
        }

        viewModel { (playerTrack: PlayerTrack) ->
            PlayerViewModel(
            playerTrack = playerTrack,
            audioPlayerInteractor = get { parametersOf(playerTrack) },
            audioPlayerDatabaseInteractor = get()
            )

        }
        factory<PlayerTrackDbConverter> { PlayerTrackDbConverter() }

        single<AudioPlayerDatabaseRepository> {
            AudioPlayerDatabaseRepositoryImpl(appDatabase = get(), playerTrackDbConverter = get())
        }

        factory<PlayerTrackDataConverter> { PlayerTrackDataConverter() }

        single<AudioPlayerDatabaseInteractor> {
            AudioPlayerDatabaseInteractorImpl(audioPlayerDatabaseRepository = get(), playerTrackDataConverter = get())
        }
    }


