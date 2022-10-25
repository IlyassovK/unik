package com.example.students.features.registration.data.model

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String
)