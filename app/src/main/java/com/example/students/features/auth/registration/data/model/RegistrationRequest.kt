package com.example.students.features.auth.registration.data.model

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("code")
    val code: String,
)