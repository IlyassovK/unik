package com.example.students.features.auth.form.domain.usecase

import com.example.students.features.auth.form.data.model.ProfileRequest
import com.example.students.features.auth.form.domain.repository.FormRepository
import com.example.students.utils.Resource

class UpdateProfileUseCase(
    private val formRepository: FormRepository,
) {

    suspend fun updateProfile(request: ProfileRequest): Resource<Boolean> {
        return formRepository.updateProfile(request)
    }
}