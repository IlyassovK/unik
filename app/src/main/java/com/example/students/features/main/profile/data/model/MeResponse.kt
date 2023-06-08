package com.example.students.features.main.profile.data.model

import com.example.students.features.main.contacts.data.model.City
import com.example.students.features.main.contacts.data.model.Hobby
import com.example.students.features.main.contacts.data.model.Speciality
import com.example.students.features.main.contacts.data.model.University
import com.google.gson.annotations.SerializedName

data class MeResponse(
    @SerializedName("data")
    val data: Data,
) {
    data class Data(
        @SerializedName("access_token")
        val accessToken: String?,
        @SerializedName("avatar")
        val avatar: String?,
        @SerializedName("birth_date")
        val birthDate: String?,
        @SerializedName("city")
        val city: City?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("hobbies")
        val hobbies: List<Hobby>?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("speciality")
        val speciality: Speciality?,
        @SerializedName("university")
        val university: University?,
    )
}