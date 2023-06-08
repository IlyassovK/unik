package com.example.students.features.auth.form.data.model

import com.google.gson.annotations.SerializedName

data class ProfileRequest(
    @SerializedName("birth_date")
    var birthDate: String,
    @SerializedName("city_id")
    var cityId: Int,
    @SerializedName("email")
    var email: String,
    @SerializedName("hobbiesIds")
    var hobbiesIds: List<Int>,
    @SerializedName("name")
    var name: String,
    @SerializedName("speciality_id")
    var specialityId: Int,
    @SerializedName("university_id")
    var universityId: Int,
)