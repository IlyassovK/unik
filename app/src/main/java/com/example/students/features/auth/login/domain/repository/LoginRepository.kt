package com.example.students.features.auth.login.domain.repository

import com.example.students.features.auth.login.data.model.LoginRequest
import com.example.students.features.auth.login.data.model.LoginResponse
import com.example.students.utils.Resource

interface LoginRepository {

    suspend fun login(request: LoginRequest): Resource<LoginResponse>

    suspend fun logout()
}