package com.example.students.utils

import android.content.Context
import android.content.res.Configuration
import timber.log.Timber
import java.util.*

object LocaleManager {

    enum class SupportedLanguages {
        ENGLISH,
        RUSSIAN,
        KAZAKH;

        fun toLocale(): Locale = Locale(isoCode())

        fun isoCode(): String {
            return when (this) {
                ENGLISH -> "en"
                RUSSIAN -> "ru"
                KAZAKH -> "kk"
            }
        }

        companion object {
            fun isSupported(langIsoCode: String): Boolean {
                return ENGLISH.isoCode() == langIsoCode
                        || RUSSIAN.isoCode() == langIsoCode
                        || KAZAKH.isoCode() == langIsoCode
            }
        }
    }


    @JvmStatic
    fun switchLanguage(context: Context?, language: SupportedLanguages) {
        Timber.d("TEMP ARA: language switched to $language")
        context?.let {
            LocalePreferences(context).switchLocale(language.toLocale())
        }
    }

    @JvmStatic
    fun currentLocale(context: Context?): Locale {
        return context?.let {
            LocalePreferences(it).getSavedLocale()
        } ?: Locale(SupportedLanguages.ENGLISH.isoCode())
    }

    @JvmStatic
    fun wrapContext(context: Context?): Context? {
        val newConfig = createConfig(context)
        return context?.createConfigurationContext(newConfig)
    }

    @JvmStatic
    fun overrideLocale(context: Context?) {
        val newConfig = createConfig(context)

        context?.resources?.updateConfiguration(newConfig, context.resources.displayMetrics)
        context?.applicationContext?.resources?.updateConfiguration(newConfig, context.resources.displayMetrics)
    }


    @JvmStatic
    private fun createConfig(context: Context?): Configuration {
        val locale = context?.let {
            LocalePreferences(it).getSavedLocale()
        } ?: Locale(SupportedLanguages.RUSSIAN.isoCode())

        Locale.setDefault(locale)

        val newConfig = Configuration()
        newConfig.setLocale(locale)

        return newConfig
    }

}