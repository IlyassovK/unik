package com.example.students.features.main.map.domain

import com.example.students.features.main.map.data.LocationRepository
import com.example.students.features.main.map.data.model.CreatePointRequest
import com.example.students.features.main.map.data.model.CreatePointResponse
import com.example.students.features.main.map.presentation.model.LocationsNearby
import com.example.students.utils.Resource
import com.example.students.utils.exceptions.NetworkExceptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocationUseCase(
    private val repository: LocationRepository,
    private val networkDispatcher: CoroutineDispatcher,
) {
    suspend fun getNearbyLocations(
        type: String,
        range: Double,
        latitude: Double,
        longitude: Double,
    ): Resource<LocationsNearby.Ui.NearbyLocation> {

        return withContext(networkDispatcher) {
            try {
                //todo temp range = 100000000
//                val rangeVal =
//                    (if (range <= 0) 1.0 else if (range > 6000.0) 6000.0 else range).toInt()
                val rangeVal = 100000000
                val nearbyLocations = repository.nearbyLocations(
                    type = type,
                    range = rangeVal,
                    latitude = latitude,
                    longitude = longitude,
                )

                if (nearbyLocations.state == Resource.State.SUCCESS) {
                    return@withContext Resource.success(data = nearbyLocations.data!!)
                } else {
                    return@withContext Resource.error(NetworkExceptions.BadRequest())
                }

            } catch (ex: Exception) {
                return@withContext Resource.error(ex, Resource.Source.SERVER)
            }
        }
    }

    suspend fun createPost(request: CreatePointRequest): Resource<CreatePointResponse> {
        return repository.createPoint(request)
    }
}