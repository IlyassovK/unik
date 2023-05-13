package com.example.students.features.main.map.domain

import com.example.students.features.main.map.data.LocationRepository
import com.example.students.features.main.map.data.LocationsApi
import com.example.students.features.main.map.data.model.CreatePointRequest
import com.example.students.features.main.map.data.model.CreatePointResponse
import com.example.students.features.main.map.presentation.model.LocationsNearby
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions

class LocationRepositoryImpl(
    private val api: LocationsApi,
) : LocationRepository {
    override suspend fun nearbyLocations(
        type: String,
        range: Int,
        latitude: Double,
        longitude: Double,
    ): Resource<LocationsNearby.Ui.NearbyLocation> {
        val result = api.nearbyLocations(
            type = type,
            range = range,
            lat = latitude,
            long = longitude,
        )

        if (result.isSuccessful) {
            val resultBody = result.body()
            return Resource.success(
                LocationsNearby.Converter.convertWebNearbyLocationsResponse2UiNearbyLocationsList(
                    resultBody!!
                )
            )
        }
        throw NetworkExceptions.BadRequest("Couldn't get locations")
    }

    override suspend fun createPoint(request: CreatePointRequest): Resource<CreatePointResponse> {
        return try {
            val result = api.createPoint(request)
            if (result.isSuccessful && result.body() != null) {
                return Resource.success(result.body()!!)
            } else {
                return Resource.error(NetworkExceptions.BadRequest("Operation is unsuccessful"))
            }
        } catch (e: Exception) {
            Resource.error(NetworkExceptions.BadRequest("Exception during create post"))
        }
    }
}