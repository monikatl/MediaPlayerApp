package com.baszczyk.mediaplayerapp.di

import com.baszczyk.mediaplayerapp.player.ForegroundManager
import com.baszczyk.mediaplayerapp.repo.AuthRepository
import com.baszczyk.mediaplayerapp.repo.AuthRepositoryImpl
import com.baszczyk.mediaplayerapp.repo.SongRepository
import com.baszczyk.mediaplayerapp.repo.SongRepositoryImpl
import com.baszczyk.mediaplayerapp.sreens.auth.AuthViewModel
import com.baszczyk.mediaplayerapp.sreens.mediaplayer.MediaPlayerViewModel
import com.baszczyk.mediaplayerapp.sreens.home.HomeViewModel
import com.baszczyk.mediaplayerapp.sreens.list.ListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {

    singleOf(::SongRepositoryImpl) bind SongRepository::class

    single<AuthRepository> {
        AuthRepositoryImpl()
    }

    viewModel {
        AuthViewModel(
            repository = get()
        )
    }

    viewModel {
        HomeViewModel(
            songRepository = get()
        )
    }

    viewModel {
        ListViewModel(
            repository = get()
        )
    }

    single {
        ForegroundManager(
            context = androidContext()
        )
    }

    viewModel {
        MediaPlayerViewModel(
            foregroundManager = get(),
            repository = get()

        )
    }
}