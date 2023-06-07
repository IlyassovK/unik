package com.example.students.utils

import android.content.Context
import java.util.*

class LocalePreferences(
    context: Context
) {

    private companion object {
        const val PREF_FILE_NAME = "smartbank_locale.preferences"
    }

    private val preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)


    private object LanguageKeys {
        const val KEY_LOCALE = "LocalePreferences.Keys.Locale"
    }


    fun switchLocale(locale: Locale) {
        saveLocale(locale)
    }

    fun getLangIsoCode(): String = getSavedLocale().language

    fun getSavedLocale(): Locale = Locale(
        preferences.getString(LanguageKeys.KEY_LOCALE, getDeviceLocale()) ?: getDeviceLocale()
    )

    private fun saveLocale(locale: Locale) {
        preferences.edit()
            .putString(LanguageKeys.KEY_LOCALE, locale.language)
            .apply()
    }

    private fun getDeviceLocale(): String {
        val deviceLocale = Locale.getDefault().language
        return if (LocaleManager.SupportedLanguages.isSupported(deviceLocale)) {
            deviceLocale
        } else {
            LocaleManager.SupportedLanguages.ENGLISH.isoCode()
        }
    }


    fun clean() {
        preferences.edit().clear().apply()
    }
}