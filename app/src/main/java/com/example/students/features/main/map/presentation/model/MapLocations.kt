package com.example.students.features.main.map.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

object MapLocations {
    @Parcelize
    data class MapObject(
        var latitude: Double = 43.25654,
        var longitude: Double = 76.92848,
        val type: Type = Type.ALL,
        val id: Long = 0,
        val name: String = "",
        val address: String = "",
        val description: String = "",
        val userId: Long = 0,
        val distance: Double = 0.0
    ) : Parcelable

    enum class Type(val value: String) {
        ALL("all"),
        Friend("friend"),
        Note("note"),
        UNKNOWN("unknown")
    }
}

