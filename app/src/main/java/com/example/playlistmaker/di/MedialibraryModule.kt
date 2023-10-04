package com.example.playlistmaker.di

import com.example.playlistmaker.medialibrary.data.converters.TrackDbConverter
import com.example.playlistmaker.medialibrary.data.repository.FavouriteLibraryRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackDataConverter
import com.example.playlistmaker.medialibrary.domain.converters.LibraryTrackToTrackConverter
import com.example.playlistmaker.favourite.domain.FavouriteLibraryInteractor
import com.example.playlistmaker.favourite.data.repository.FavouriteLibraryRepository
import com.example.playlistmaker.medialibrary.data.repository.PlaylistMediaDatabaseRepositoryImpl
import com.example.playlistmaker.medialibrary.domain.db.PlaylistMediaDatabaseInteractor
import com.example.playlistmaker.medialibrary.domain.db.PlaylistMediaDatabaseRepository
import com.example.playlistmaker.medialibrary.domain.impl.LibraryInteractorImpl
import com.example.playlistmaker.medialibrary.domain.impl.PlaylistMediaDatabaseInteractorImpl
import com.example.playlistmaker.medialibrary.presentation.MedialibraryFavouritesViewModel
import com.example.playlistmaker.medialibrary.presentation.MedialibraryPlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val medialibraryModule = module {

    viewModel {
        MedialibraryFavouritesViewModel(libraryInteractor = get(), libraryTrackToTrackConverter = get())
    }

    viewModel {
        MedialibraryPlaylistsViewModel(playlistMediaDatabaseInteractor = get())
    }

    factory<TrackDbConverter> { TrackDbConverter() }

        factory<LibraryTrackToTrackConverter> { LibraryTrackToTrackConverter() }

        single<FavouriteLibraryRepository> {
            FavouriteLibraryRepositoryImpl(appDatabase = get(), trackDbConverter = get())
        }

        factory<LibraryTrackDataConverter> { LibraryTrackDataConverter() }

        single<FavouriteLibraryInteractor> {
            LibraryInteractorImpl(libraryDatabaseRepository = get(), libraryTrackDataConverter = get())
        }

    single<PlaylistMediaDatabaseRepository> {
        PlaylistMediaDatabaseRepositoryImpl(playlistDatabase = get())
    }

    single<PlaylistMediaDatabaseInteractor> {
        PlaylistMediaDatabaseInteractorImpl(playlistMediaDatabaseRepository = get())
    }
    }