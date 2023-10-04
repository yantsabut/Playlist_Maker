package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.favourite.data.FavouriteRepository
import com.example.playlistmaker.favourite.data.FavouriteRepositoryImpl
import com.example.playlistmaker.favourite.domain.FavouriteInteractor
import com.example.playlistmaker.favourite.domain.FavouriteInteractorImpl
import com.example.playlistmaker.player.data.converters.PlayerTrackDbConverter
import com.example.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.data.repository.PlaylistTrackDatabaseRepositoryImpl
import com.example.playlistmaker.player.domain.converters.PlayerTrackDataConverter
import com.example.playlistmaker.player.domain.interactors.AudioPlayerInteractorImpl
import com.example.playlistmaker.player.data.interactors.PlaylistTrackDatabaseInteractorImpl
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerInteractor
import com.example.playlistmaker.player.domain.interfaces.AudioPlayerRepository
import com.example.playlistmaker.player.data.interactors.PlaylistTrackDatabaseInteractor
import com.example.playlistmaker.player.data.repository.PlaylistTrackDatabaseRepository
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
            audioPlayerDatabaseInteractor = get(),
            playlistMediaDatabaseInteractor = get(),
            playlistTrackDatabaseInteractor = get(),
            playlistDatabaseInteractor = get()
        )
    }
        factory<PlayerTrackDbConverter> { PlayerTrackDbConverter() }


        single<FavouriteRepository> {
            FavouriteRepositoryImpl(appDatabase = get(), playerTrackDbConverter = get())
        }

        factory<PlayerTrackDataConverter> { PlayerTrackDataConverter() }

        single<FavouriteInteractor> {
            FavouriteInteractorImpl(audioPlayerDatabaseRepository = get(), playerTrackDataConverter = get())
        }

    single<PlaylistTrackDatabaseRepository> {
        PlaylistTrackDatabaseRepositoryImpl(playlistTrackDatabase = get())
    }

    single<PlaylistTrackDatabaseInteractor> {
        PlaylistTrackDatabaseInteractorImpl(playlistTrackDatabaseRepository = get())
    }
    }


