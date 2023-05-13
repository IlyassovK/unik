package com.example.students.features.main.map.data

import com.example.students.features.main.map.data.model.CreatePointRequest
import com.example.students.features.main.map.data.model.CreatePointResponse
import com.example.students.features.main.map.presentation.model.LocationsNearby
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LocationsApi {
    @GET("/api/map/points/places/{type}/{lat}/{long}/{range}")
    suspend fun nearbyLocations(
        @Path("type") type: String,
        @Path("lat") lat: Double,
        @Path("long") long: Double,
        @Path("range") range: Int,
    ): Response<LocationsNearby.Web.NearbyLocation>

    @POST("/api/map/points/create")
    suspend fun createPoint(
        @Body request: CreatePointRequest,
    ): Response<CreatePointResponse>
}