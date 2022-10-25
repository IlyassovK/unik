package com.example.students.features.form.presentation

import com.example.students.features.form.data.model.ProfileRequest
import com.google.gson.annotations.SerializedName

data class ProfileInfo(
    val birthDate: String,
    val city: String,
    val email: String,
    val hobbies: List<String>,
    val name: String,
    val speciality: String,
    val university: String
) {
    fun convert2ProfileRequest(): ProfileRequest {
        return ProfileRequest(
            birthDate = this.birthDate,
            cityId = 1,
            email = this.email,
            hobbiesIds = listOf(1),
            name = this.name,
            specialityId = 1,
            universityId = 1
        )
    }
}