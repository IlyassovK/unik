package com.example.students.features.auth.registration.domain

import com.example.students.features.auth.registration.data.model.RegistrationRequest
import com.example.students.features.auth.registration.data.model.RegistrationResponse
import com.example.students.features.auth.registration.domain.repository.RegistrationRepository
import com.example.students.utils.Resource

class RegistrationUseCase(
    private val registrationRepository: RegistrationRepository
) {
    suspend fun registration(request: RegistrationRequest): Resource<RegistrationResponse> {
        return registrationRepository.registration(request)
    }
}