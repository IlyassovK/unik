package com.example.students.features.form.data.repository

import com.example.students.features.form.data.model.ProfileRequest
import com.example.students.features.form.data.model.ProfileResponse
import com.example.students.features.form.data.service.FormApi
import com.example.students.features.form.domain.repository.FormRepository
import com.example.students.utils.GlobalPreferences
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import java.lang.Exception

class FormRepositoryImpl(
    private val preference: GlobalPreferences,
    private val formApi: FormApi
) : FormRepository {

    override suspend fun updateProfile(
        profileRequest: ProfileRequest
    ): Resource<ProfileResponse> {
        return try {
            val result = formApi.updateProfile(
                id = preference.getUserId().toLong(),
                profileRequest = profileRequest
            )
            return Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during registration"))
        }
    }

}