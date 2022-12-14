package com.example.students.features.registration.data.repository

import com.example.students.features.registration.data.RegistrationApi
import com.example.students.features.registration.data.model.RegistrationRequest
import com.example.students.features.registration.data.model.RegistrationResponse
import com.example.students.features.registration.domain.repository.RegistrationRepository
import com.example.students.utils.GlobalPreferences
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import java.lang.Exception

class RegistrationRepositoryImpl(
    private val preferences: GlobalPreferences,
    private val registrationApi: RegistrationApi
) : RegistrationRepository {

    override suspend fun registration(request: RegistrationRequest): Resource<RegistrationResponse> {
        return try {
            val result = registrationApi.registration(
                request = request
            )
            if (result.isSuccessful) {
                val responseBody = result.body()
                preferences.setUserPhone(responseBody!!.phone)
                preferences.setUserId(responseBody.id.toLong())
                Resource.success(responseBody)
            } else {
                Resource.error(NetworkExceptions.BadRequest(result.message()))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during registration"))
        }
    }
}