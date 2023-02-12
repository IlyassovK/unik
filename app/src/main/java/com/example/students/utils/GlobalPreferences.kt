package com.example.students.utils

import android.content.SharedPreferences
import org.koin.java.KoinJavaComponent.inject

class GlobalPreferences {

    private val preferences: SharedPreferences by inject(SharedPreferences::class.java)

    fun getAccessToken(): String {
        return preferences.getString(KEY_ACCESS_TOKEN, "") ?: ""
    }

    fun isFirstLaunch(): Boolean {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true)
    }

    fun setAccessToken(token: String) {
        preferences.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun getUserPhone(): String {
        return preferences.getString(KEY_USER_PHONE, "") ?: ""
    }

    fun setUserPhone(phone: String) {
        preferences.edit().putString(KEY_USER_PHONE, phone).apply()
    }

    fun getUserId(): String {
        return preferences.getString(KEY_USER_ID, "") ?: ""
    }

    fun setUserId(id: Long) {
        preferences.edit().putLong(KEY_USER_ID, id).apply()
    }

    fun firstStartHappened(){
        preferences.edit().putBoolean(IS_FIRST_LAUNCH, false).apply()
    }

    companion object {
        const val PREF_NAME = "preferences"
        const val IS_FIRST_LAUNCH = "is_first_launch"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_USER_PHONE = "phone_number"
        const val KEY_USER_ID = "phone_number"
    }
}