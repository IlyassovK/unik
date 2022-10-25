package com.example.students

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.example.students.di.network.commonModule
import com.example.students.di.network.networkModule
import com.example.students.features.form.di.formModule
import com.example.students.features.login.di.loginModule
import com.example.students.features.registration.di.registrationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initLogger()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                loginModule,
                formModule,
                registrationModule,
                commonModule
            )
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}