package com.example.students.features.main.map.data

import com.example.students.features.main.map.data.model.CreatePointRequest
import com.example.students.features.main.map.data.model.CreatePointResponse
import com.example.students.features.main.map.presentation.model.LocationsNearby
import com.example.students.utils.Resource

interface LocationRepository {
    suspend fun nearbyLocations(
        type: String,
        range: Int,
        latitude: Double,
        longitude: Double,
    ): Resource<LocationsNearby.Ui.NearbyLocation>

    suspend fun createPoint(request: CreatePointRequest): Resource<CreatePointResponse>
}