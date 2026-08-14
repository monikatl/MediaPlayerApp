package com.baszczyk.mediaplayerapp.di

import com.baszczyk.mediaplayerapp.player.ForegroundManager
import com.baszczyk.mediaplayerapp.repo.SongRepository
import com.baszczyk.mediaplayerapp.sreens.mediaplayer.MediaPlayerViewModel
import com.baszczyk.mediaplayerapp.sreens.home.HomeViewModel
import com.baszczyk.mediaplayerapp.sreens.list.ListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        HomeViewModel()
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

    single {
        SongRepository()
    }

}