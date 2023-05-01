package com.example.students.features.auth.form.domain.usecase

import com.example.students.features.auth.form.data.model.ProfileRequest
import com.example.students.features.auth.form.domain.repository.FormRepository
import com.example.students.features.auth.form.presentation.model.FormData
import com.example.students.utils.Resource

class FormDataUseCase(
    private val formRepository: FormRepository,
) {
    suspend fun getAllUniversities(): Resource<List<FormData>> {
        return formRepository.getAllUniversities()
    }

    suspend fun getAllSpecialities(): Resource<List<FormData>> {
        return formRepository.getAllSpecialities()
    }

    suspend fun getAllHobbies(): Resource<List<FormData>> {
        return formRepository.getAllHobbies()
    }

    suspend fun getAllCities(): Resource<List<FormData>> {
        return formRepository.getAllCities()
    }
}