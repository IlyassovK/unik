package com.example.students.features.auth.form.data.model

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("birth_date")
    val birthDate: String,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("hobbiesIds")
    val hobbiesIds: List<Int>,
    @SerializedName("name")
    val name: String,
    @SerializedName("speciality_id")
    val specialityId: Int,
    @SerializedName("university_id")
    val universityId: Int
)