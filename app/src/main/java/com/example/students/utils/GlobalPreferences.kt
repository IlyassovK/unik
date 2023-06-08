package com.example.students.utils

import android.content.SharedPreferences
import com.example.students.features.auth.login.data.model.LoginResponse
import org.koin.java.KoinJavaComponent.inject

class GlobalPreferences {

    private val preferences: SharedPreferences by inject(SharedPreferences::class.java)

    fun getAccessToken(): String {
        return preferences.getString(KEY_ACCESS_TOKEN, "") ?: ""
    }

    fun isFirstLaunch(): Boolean {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true)
    }

    fun authorization(id: Long, token: String) {
        setAccessToken(token)
        setUserId(id)
        preferences.edit().putBoolean(IS_AUTHORIZED, true).apply()
    }

    fun isAuthorized(): Boolean {
        return preferences.getBoolean(IS_AUTHORIZED, false) && getAccessToken().isNotBlank()
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

    fun getUserId(): Long {
        return preferences.getLong(KEY_USER_ID, 0L)
    }

    fun setUserId(id: Long) {
        preferences.edit().putLong(KEY_USER_ID, id).apply()
    }

    fun firstStartHappened() {
        preferences.edit().putBoolean(IS_FIRST_LAUNCH, false).apply()
    }

    fun saveDeviceToken(token: String) {
        preferences.edit().putString(DEVICE_TOKEN, token).apply()
    }

    fun getDeviceToken(): String {
        return preferences.getString(DEVICE_TOKEN, "") ?: ""
    }

    fun clear() {
        setAccessToken("")
        setUserId(0)
        setUserPhone("")
        preferences.edit().putBoolean(IS_AUTHORIZED, true).apply()
    }

    companion object {
        const val PREF_NAME = "preferences"
        const val IS_FIRST_LAUNCH = "is_first_launch"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_USER_PHONE = "phone_number"
        const val KEY_USER_ID = "phone_number"
        const val IS_AUTHORIZED = "is_authorized"
        const val DEVICE_TOKEN = "device_token"
    }
}