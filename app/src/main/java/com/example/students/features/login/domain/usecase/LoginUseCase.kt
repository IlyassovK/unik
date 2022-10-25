package com.example.students.features.login.domain.usecase

import com.example.students.features.login.data.model.LoginRequest
import com.example.students.features.form.domain.repository.FormRepository
import com.example.students.features.login.data.model.LoginResponse
import com.example.students.features.login.domain.repository.LoginRepository
import com.example.students.utils.Resource
import timber.log.Timber

class LoginUseCase(
    private val loginRepository: LoginRepository
) {
    suspend fun login(request: LoginRequest): Resource<LoginResponse> {
        return loginRepository.login(request)
    }
}