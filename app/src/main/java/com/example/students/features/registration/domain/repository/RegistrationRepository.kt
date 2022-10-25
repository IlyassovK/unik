package com.example.students.features.registration.domain.repository

import com.example.students.features.registration.data.model.RegistrationRequest
import com.example.students.features.registration.data.model.RegistrationResponse
import com.example.students.utils.Resource

interface RegistrationRepository {
    suspend fun registration(request: RegistrationRequest): Resource<RegistrationResponse>
}