package com.example.students.features.auth.login.data.repository

import android.util.Log
import com.example.students.features.auth.login.data.model.LoginRequest
import com.example.students.features.auth.login.data.model.LoginResponse
import com.example.students.features.auth.login.data.service.LoginApi
import com.example.students.features.auth.login.domain.repository.LoginRepository
import com.example.students.utils.GlobalPreferences
import com.example.students.utils.exceptions.NetworkExceptions
import com.example.students.utils.Resource
import timber.log.Timber
import java.lang.Exception

class LoginRepositoryImpl(
    private val preferences: GlobalPreferences,
    private val loginApi: LoginApi,
) : LoginRepository {

    override suspend fun login(request: LoginRequest): Resource<LoginResponse> {
        return try {
            val result = loginApi.login(
                request = request
            )
            if (result.isSuccessful) {
                val responseBody = result.body()!!

                preferences.authorization(
                    token = responseBody.tokenData.original.accessToken,
                    id = responseBody.id
                )

                Resource.success(responseBody)
            } else {
                Resource.error(NetworkExceptions.BadRequest(result.message()))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during registration"))
        }
    }

    override suspend fun logout() {
        loginApi.logout()
    }
}