package com.baszczyk.mediaplayerapp

import android.app.Application
import com.baszczyk.mediaplayerapp.data.supabase.SupabaseProvider
import com.baszczyk.mediaplayerapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MediaPlayerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        SupabaseProvider.initialize(
            url = BuildConfig.SUPABASE_URL,
            key = BuildConfig.SUPABASE_KEY
        )

        startKoin {
            androidContext(this@MediaPlayerApp)
            modules(appModule)
        }
    }
}