package com.example.playlistmaker.di

import com.example.playlistmaker.playlist_info.data.db.CurrentPlaylistTracksDatabaseRepositoryImpl
import com.example.playlistmaker.playlist_info.domain.db.CurrentPlaylistTracksDatabaseInteractor
import com.example.playlistmaker.playlist_info.domain.db.CurrentPlaylistTracksDatabaseRepository
import com.example.playlistmaker.playlist_info.domain.impl.CurrentPlaylistTracksDatabaseInteractorImpl
import com.example.playlistmaker.playlist_info.presentation.PlaylistInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistInfoModule = module {

    single<CurrentPlaylistTracksDatabaseRepository> {
        CurrentPlaylistTracksDatabaseRepositoryImpl(playlistTrackDatabase = get())
    }

    single<CurrentPlaylistTracksDatabaseInteractor> {
        CurrentPlaylistTracksDatabaseInteractorImpl(currentPlaylistTracksDatabaseRepository = get())
    }

    viewModel {
        PlaylistInfoViewModel(
            currentPlaylistTracksDatabaseInteractor = get(),
            playlistDatabaseInteractor = get(),
            playlistTrackDatabaseInteractor = get(),
            playlistMediaDatabaseInteractor = get()
        )
    }

}