package com.example.students.features.auth.registration.data.model

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("data")
    val data: Data,
)

data class Data(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?,
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("updated_at")
    val updated_at: String?,
)