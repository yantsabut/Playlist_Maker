package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.data.converters.TrackDbConverter
import com.example.playlistmaker.medialibrary.data.repository.LibraryDatabaseRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackDataConverter
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackToTrackConverter
import com.example.playlistmaker.favourite.domain.FavouriteLibraryInteractor
import com.example.playlistmaker.favourite.data.FavouriteLibraryRepository
import com.example.playlistmaker.medialibrary.domain.impl.LibraryInteractorImpl
import com.example.playlistmaker.medialibrary.presentation.MedialibraryFavouritesViewModel
import com.example.playlistmaker.medialibrary.presentation.MedialibraryPlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val medialibraryModule = module {

    viewModel {
        MedialibraryFavouritesViewModel(libraryInteractor = get(), libraryTrackToTrackConverter = get())
    }

    viewModel {
        MedialibraryPlaylistsViewModel()
    }

    factory<TrackDbConverter> { TrackDbConverter() }

        factory<LibraryTrackToTrackConverter> { LibraryTrackToTrackConverter() }

        single<FavouriteLibraryRepository> {
            LibraryDatabaseRepositoryImpl(appDatabase = get(), trackDbConverter = get())
        }

        factory<LibraryTrackDataConverter> { LibraryTrackDataConverter() }

        single<FavouriteLibraryInteractor> {
            LibraryInteractorImpl(libraryDatabaseRepository = get(), libraryTrackDataConverter = get())
        }

    }