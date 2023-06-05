package com.example.students.features.main.profile.data.model

import com.example.students.features.main.contacts.data.model.City
import com.example.students.features.main.contacts.data.model.Hobby
import com.example.students.features.main.contacts.data.model.Speciality
import com.example.students.features.main.contacts.data.model.University
import com.google.gson.annotations.SerializedName

data class FriendsResponse(
    @SerializedName("data")
    val data: List<Data>,
) {
    data class Data(
        @SerializedName("friend_id")
        val friendId: Int,
        @SerializedName("friends")
        val friend: Friend,
        @SerializedName("user")
        val user: Friend,
        @SerializedName("id")
        val id: Int,
        @SerializedName("status")
        val status: Int,
        @SerializedName("user_id")
        val userId: Int,
    )
}

data class Friend(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("birth_date")
    val birthDate: String,
    @SerializedName("city")
    val city: City,
    @SerializedName("email")
    val email: String,
    @SerializedName("hobbies")
    val hobbies: List<Hobby>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("speciality")
    val speciality: Speciality,
    @SerializedName("university")
    val university: University,
)

