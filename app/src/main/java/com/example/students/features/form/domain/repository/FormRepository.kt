package com.example.students.features.form.domain.repository

import com.example.students.features.form.data.model.FormDataResponse
import com.example.students.features.form.data.model.ProfileRequest
import com.example.students.features.form.data.model.ProfileResponse
import com.example.students.features.form.presentation.model.FormData
import com.example.students.utils.Resource

interface FormRepository {

    suspend fun updateProfile(profileRequest: ProfileRequest): Resource<ProfileResponse>

    suspend fun getAllUniversities(): Resource<List<FormData>>
    suspend fun getAllSpecialities(): Resource<List<FormData>>
    suspend fun getAllHobbies(): Resource<List<FormData>>
    suspend fun getAllCities(): Resource<List<FormData>>

}