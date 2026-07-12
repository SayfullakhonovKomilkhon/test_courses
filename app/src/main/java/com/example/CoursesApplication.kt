package com.example

import android.app.Application
import com.example.core.database.databaseModule
import com.example.core.network.networkModule
import com.example.di.appModule
import com.example.feature.auth.di.authModule
import com.example.feature.onboarding.di.onboardingModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CoursesApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CoursesApplication)
            modules(
                databaseModule,
                networkModule,
                onboardingModule,
                authModule,
                appModule
            )
        }
    }
}
