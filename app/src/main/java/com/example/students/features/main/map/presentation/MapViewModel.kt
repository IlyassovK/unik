package com.example.students.features.main.map.presentation

import androidx.lifecycle.ViewModel
import com.example.students.features.main.map.presentation.model.Location

class MapViewModel : ViewModel() {

    var lastLocation: Location = Location(0.0, 0.0)

    fun updateCurrentLocation(latitude: Double, longitude: Double) {
        lastLocation = Location(
            latitude = latitude,
            longitude = longitude
        )
    }
}