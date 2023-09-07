package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.data.converters.TrackDbConverter
import com.example.playlistmaker.medialibrary.data.repository.LibraryDatabaseRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackDataConverter
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackToTrackConverter
import com.example.playlistmaker.medialibrary.domain.db.LibraryInteractor
import com.example.playlistmaker.medialibrary.domain.db.LibraryRepository
import com.example.playlistmaker.medialibrary.domain.impl.LibraryDatabaseInteractorImpl
import com.example.playlistmaker.medialibrary.presentation.MedialibraryFavouritesViewModel
import com.example.playlistmaker.medialibrary.presentation.MedialibraryPlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val medialibraryModule = module {

    viewModel {
        MedialibraryFavouritesViewModel(libraryDatabaseInteractor = get(), libraryTrackToTrackConverter = get())
    }

    viewModel {
        MedialibraryPlaylistsViewModel()
    }

    factory<TrackDbConverter> { TrackDbConverter() }

        factory<LibraryTrackToTrackConverter> { LibraryTrackToTrackConverter() }

        single<LibraryRepository> {
            LibraryDatabaseRepositoryImpl(appDatabase = get(), trackDbConverter = get())
        }

        factory<LibraryTrackDataConverter> { LibraryTrackDataConverter() }

        single<LibraryInteractor> {
            LibraryDatabaseInteractorImpl(libraryDatabaseRepository = get(), libraryTrackDataConverter = get())
        }

    }