package com.example.students.features.auth.registration.data.model

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("updated_at")
    val updatedAt: String
)