package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.presentation.MedialibraryPlaylistsViewModel
import com.example.playlistmaker.medialibrary.presentation.MedialibraryFavouritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val medialibraryModule = module {

    viewModel {
        MedialibraryFavouritesViewModel()
    }

    viewModel {
      MedialibraryPlaylistsViewModel()
    }
}