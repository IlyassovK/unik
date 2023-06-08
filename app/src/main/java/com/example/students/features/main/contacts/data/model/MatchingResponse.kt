package com.example.students.features.main.contacts.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MatchingResponse(
    @SerializedName("user")
    val user: User?,
)

@Parcelize
data class User(
    @SerializedName("birth_date")
    val birthDate: String?,
    @SerializedName("city")
    val city: City?,
    @SerializedName("cityId")
    val city_id: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: String?,
    @SerializedName("hobbies")
    val hobbies: List<Hobby>?,
    @SerializedName("hobbies_count")
    val hobbiesCount: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("phone_verified_at")
    val phoneVerifiedAt: String?,
    @SerializedName("speciality")
    val speciality: Speciality?,
    @SerializedName("specialityId")
    val speciality_id: Int?,
    @SerializedName("university")
    val university: University?,
    @SerializedName("universityId")
    val university_id: Int?,
    @SerializedName("updatedAt")
    val updated_at: String?,
    @SerializedName("uuid")
    val uuid: String?,
    @SerializedName("avatar")
    val avatar: String?,
): Parcelable

@Parcelize
data class City(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
): Parcelable

@Parcelize
data class Hobby(
    @SerializedName("created_at")
    val created_at: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
): Parcelable

@Parcelize
data class Speciality(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
): Parcelable

@Parcelize
data class University(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
): Parcelable