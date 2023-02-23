package com.example.students.features.form.domain.usecase

import com.example.students.features.form.data.model.ProfileRequest
import com.example.students.features.form.domain.repository.FormRepository

class UpdateProfileUseCase(
    private val formRepository: FormRepository
) {

    suspend fun updateProfile(request: ProfileRequest) {
        formRepository.updateProfile(request)
    }
}