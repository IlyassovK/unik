package com.example.students.features.auth.login.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("code")
    val code: String,
)