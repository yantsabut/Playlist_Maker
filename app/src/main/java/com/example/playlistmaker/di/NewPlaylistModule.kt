package com.example.playlistmaker.di

import com.example.playlistmaker.new_playlist.data.repository.PlaylistDatabaseRepositoryImpl
import com.example.playlistmaker.new_playlist.data.repository.PlaylistDatabaseInteractor
import com.example.playlistmaker.new_playlist.data.repository.PlaylistDatabaseRepository
import com.example.playlistmaker.new_playlist.domain.impl.PlaylistDatabaseInteractorImpl
import com.example.playlistmaker.new_playlist.presentation.NewPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPlaylistModule = module {

    viewModel{
        NewPlaylistViewModel(playlistDatabaseInteractor = get())
    }

    single<PlaylistDatabaseRepository> {
        PlaylistDatabaseRepositoryImpl(playlistDatabase = get())
    }

    single<PlaylistDatabaseInteractor> {
        PlaylistDatabaseInteractorImpl(playlistDatabaseRepository = get())
    }
}
