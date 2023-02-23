package com.example.students.features.auth.form.domain.usecase

import com.example.students.features.auth.form.data.model.ProfileRequest
import com.example.students.features.auth.form.domain.repository.FormRepository

class UpdateProfileUseCase(
    private val formRepository: FormRepository
) {

    suspend fun updateProfile(request: ProfileRequest) {
        formRepository.updateProfile(request)
    }
}