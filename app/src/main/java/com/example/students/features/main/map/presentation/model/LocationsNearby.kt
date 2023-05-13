package com.example.students.features.main.map.presentation.model

import com.google.gson.annotations.SerializedName

object LocationsNearby {

    object Web {
        data class NearbyLocation(
            @SerializedName("friends")
            val friends: List<Friend>,
            @SerializedName("notes")
            val notes: List<Note>,
        )

        data class Friend(
            @SerializedName("address")
            val address: String,
            @SerializedName("id")
            val id: Long,
            @SerializedName("latitude")
            val latitude: Double,
            @SerializedName("longitude")
            val longitude: Double,
            @SerializedName("name")
            val name: String,
            @SerializedName("created_at")
            val created_at: String,
            @SerializedName("deleted_at")
            val deleted_at: Any,
            @SerializedName("laravel_through_key")
            val laravel_through_key: Int,
            @SerializedName("updated_at")
            val updated_at: String,
            @SerializedName("user_id")
            val user_id: Long,
            @SerializedName("distance")
            val distance: Double,
        )

        data class Note(
            @SerializedName("address")
            val address: String,
            @SerializedName("id")
            val id: Long,
            @SerializedName("latitude")
            val latitude: Double,
            @SerializedName("longitude")
            val longitude: Double,
            @SerializedName("name")
            val name: String,
            @SerializedName("placeDescription")
            val placeDescription: String,
            @SerializedName("created_at")
            val created_at: String,
            @SerializedName("deleted_at")
            val deleted_at: Any,
            @SerializedName("place_description")
            val place_description: String,
            @SerializedName("updated_at")
            val updated_at: String,
            @SerializedName("user_id")
            val user_id: Long,
            @SerializedName("distance")
            val distance: Double,
        )

    }

    object Ui {
        data class NearbyLocation(
            val friends: List<Friend>,
            val notes: List<Note>,
        )

        data class Friend(
            val address: String,
            val id: Long,
            val latitude: Double,
            val longitude: Double,
            val name: String,
            val userId: Long,
            val distance: Double,
        )

        data class Note(
            val address: String,
            val id: Long,
            val latitude: Double,
            val longitude: Double,
            val name: String,
            val placeDescription: String,
            val userId: Long,
            val distance: Double,
        )

    }

    object Converter {

        fun convertWebNearbyLocationsResponse2UiNearbyLocationsList(
            webResponse: LocationsNearby.Web.NearbyLocation,
        ): Ui.NearbyLocation {
            return Ui.NearbyLocation(
                friends = webResponse.friends.let { list ->
                    list.map { convertWebNearbyLocationsFriends2Ui(it) }
                },
                notes = webResponse.notes.let { list ->
                    list.map { convertWebNearbyLocationsNotes2Ui(it) }
                },
            )
        }

        private fun convertWebNearbyLocationsFriends2Ui(webItem: Web.Friend): Ui.Friend {
            return Ui.Friend(
                address = webItem.address, id = webItem.id,
                latitude = webItem.latitude, longitude = webItem.longitude,
                name = webItem.name,
                userId = webItem.user_id,
                distance = webItem.distance
            )

        }

        private fun convertWebNearbyLocationsNotes2Ui(webItem: Web.Note): Ui.Note {
            return Ui.Note(
                address = webItem.address, id = webItem.id,
                latitude = webItem.latitude, longitude = webItem.longitude,
                name = webItem.name,
                userId = webItem.user_id, placeDescription = webItem.place_description,
                distance = webItem.distance
            )
        }
    }

}