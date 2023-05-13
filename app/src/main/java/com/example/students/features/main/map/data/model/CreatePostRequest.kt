package com.example.students.features.main.map.data.model

import com.google.gson.annotations.SerializedName

data class CreatePointRequest(
    @SerializedName("latitude")
    private val latitude: Double,
    @SerializedName("longitude")
    private val longitude: Double,
    @SerializedName("name")
    private val name: String,
    @SerializedName("address")
    private val address: String = "a",
    @SerializedName("place_description")
    private val placeDescription: String,
)