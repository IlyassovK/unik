package com.example.students.features.map.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.students.R
import com.example.students.databinding.FragmentMapBinding
import com.example.students.utils.PermissionUtil
import com.google.android.gms.location.*
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.RotationType
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider


class MapFragment : Fragment(), UserLocationObjectListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var locationProviderClient: FusedLocationProviderClient
    private val viewModel: MapViewModel by viewModels()
    private lateinit var userLocationLayer: UserLocationLayer

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
            }
            else -> {
                // No location access granted.
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (PermissionUtil.checkLocationPermissions(requireContext())) {
            if (isLocationEnabled()) {
                locationProviderClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if (location == null || (location.latitude == 0.0 && location.longitude == 0.0)) {
                        requestNewLocationData()
                    } else {
                        viewModel.updateCurrentLocation(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )


//                        binding.mapView.map.move(
//                            CameraPosition(Point(viewModel.lastLocation.latitude,
//                                viewModel.lastLocation.longitude), 11.0f, 0.0f, 0.0f),
//                            Animation(Animation.Type.SMOOTH, 10f),
//                            null)
                    }
                }
            } else {
                Toast.makeText(requireContext(),
                    "Please turn on your location...",
                    Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(5)
            .setFastestInterval(0)
            .setNumUpdates(1)

        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationProviderClient.requestLocationUpdates(mLocationRequest,
            mLocationCallback,
            Looper.myLooper())
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            mLastLocation?.let {
                viewModel.updateCurrentLocation(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
                Log.d("KRM:", "location 2 ${viewModel.lastLocation}")

                binding.mapView.map.move(
                    CameraPosition(Point(viewModel.lastLocation.latitude,
                        viewModel.lastLocation.longitude), 11.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 1000f),
                    null)
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun initMap() {
        MapKitFactory.initialize(requireActivity())
        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()
        userLocationLayer = mapKit.createUserLocationLayer(binding.mapView.mapWindow);
        userLocationLayer.isVisible = true;
        userLocationLayer.isHeadingEnabled = true;

        userLocationLayer.setObjectListener(this);
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer.setAnchor(
            PointF((binding.mapView.width * 0.5).toFloat(),
                (binding.mapView.height * 0.5).toFloat()),
            PointF((binding.mapView.width * 0.5).toFloat(),
                (binding.mapView.height * 0.83).toFloat()))
        userLocationView.arrow.setIcon(ImageProvider.fromResource(
            requireContext(), R.drawable.icv_user_arrow))
        val pinIcon = userLocationView.pin.useCompositeIcon()
        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource(requireContext(), R.drawable.icv_map_marker),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )
        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(requireContext(), R.drawable.icv_person),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )
        userLocationView.accuracyCircle.fillColor = Color.BLUE
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }
}