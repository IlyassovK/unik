package com.example.students.features.login.domain.repository

import com.example.students.features.login.data.model.LoginRequest
import com.example.students.features.login.data.model.LoginResponse
import com.example.students.utils.Resource

interface LoginRepository {

    suspend fun login(request: LoginRequest): Resource<LoginResponse>

    suspend fun logout()
}