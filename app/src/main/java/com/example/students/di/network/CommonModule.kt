package com.example.students.di.network

import android.app.Application
import android.content.SharedPreferences
import com.example.students.utils.GlobalPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val commonModule = module {
    single{
        getSharedPrefs(androidApplication())
    }

    single { GlobalPreferences() }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return  androidApplication.getSharedPreferences(GlobalPreferences.PREF_NAME,  android.content.Context.MODE_PRIVATE)
}