package com.example.students.features.auth.login.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("token_data")
    val tokenData: TokenData,
)

data class TokenData(
    @SerializedName("exception")
    val exception: String,
    @SerializedName("token_cdata")
    val original: Original,
)

data class Original(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("token_type")
    val tokenType: String,
)