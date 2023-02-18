package com.example.students.features.auth.login.domain.usecase

import com.example.students.features.auth.login.data.model.LoginRequest
import com.example.students.features.auth.login.data.model.LoginResponse
import com.example.students.features.auth.login.domain.repository.LoginRepository
import com.example.students.utils.Resource

class LoginUseCase(
    private val loginRepository: LoginRepository,
) {
    suspend fun login(request: LoginRequest): Resource<LoginResponse> {
        return loginRepository.login(request)
    }
}