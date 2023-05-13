package com.example.students.features.main.map.data.model

import com.google.gson.annotations.SerializedName

data class CreatePointResponse(
    @SerializedName("location")
    val location: List<Location>,
    @SerializedName("message")
    val message: String,
)

data class Location(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deletedAt")
    val deletedAt: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("place_description")
    val placeDescription: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: Int,
)