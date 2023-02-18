package com.example.students.features.form.domain.repository

import com.example.students.features.form.data.model.ProfileRequest
import com.example.students.features.form.data.model.ProfileResponse
import com.example.students.utils.Resource

interface FormRepository {

    suspend fun updateProfile(profileRequest: ProfileRequest): Resource<ProfileResponse>
}