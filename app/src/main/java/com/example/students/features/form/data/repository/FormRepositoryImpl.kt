package com.example.students.features.form.data.repository

import com.example.students.features.form.data.model.Converter
import com.example.students.features.form.data.model.FormDataResponse
import com.example.students.features.form.data.model.ProfileRequest
import com.example.students.features.form.data.model.ProfileResponse
import com.example.students.features.form.data.service.FormApi
import com.example.students.features.form.domain.repository.FormRepository
import com.example.students.features.form.presentation.model.FormData
import com.example.students.utils.GlobalPreferences
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import kotlin.Exception

class FormRepositoryImpl(
    private val preference: GlobalPreferences,
    private val formApi: FormApi,
) : FormRepository {

    override suspend fun updateProfile(
        profileRequest: ProfileRequest,
    ): Resource<ProfileResponse> {
        return try {
            val result = formApi.updateProfile(
                profileRequest = profileRequest
            )
            return Resource.success(result)
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during registration"))
        }
    }

    override suspend fun getAllUniversities(): Resource<List<FormData>> {
        return try {
            val result = formApi.getUniversities()
            return Resource.success(Converter.convert2Ui(result))
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting universities"))
        }
    }

    override suspend fun getAllSpecialities(): Resource<List<FormData>> {
        return try {
            val result = formApi.getSpecialities()
            return Resource.success(Converter.convert2Ui(result))
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting specialities"))
        }
    }

    override suspend fun getAllHobbies(): Resource<List<FormData>> {
        return try {
            val result = formApi.getHobbies()
            return Resource.success(Converter.convert2Ui(result))
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting hobbies"))
        }
    }

    override suspend fun getAllCities(): Resource<List<FormData>> {
        return try {
            val result = formApi.getCities()
            return Resource.success(Converter.convert2Ui(result))
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during getting cities"))
        }
    }

}