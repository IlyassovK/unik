package com.example.students

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.example.students.di.network.commonModule
import com.example.students.di.network.networkModule
import com.example.students.features.feed.di.feedPageModule
import com.example.students.features.form.di.formModule
import com.example.students.features.login.di.loginModule
import com.example.students.features.otp.di.otpModule
import com.example.students.features.registration.di.registrationModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initLogger()
        initYandexMap()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            val modules = modules(
                networkModule,
                loginModule,
                formModule,
                registrationModule,
                commonModule,
                otpModule,
                feedPageModule
            )
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initYandexMap() {
        //Yandex Mapkit setup
        MapKitFactory.setApiKey("c71fc5cc-c72f-4cfa-847f-fd354f82f5d8")
    }
}