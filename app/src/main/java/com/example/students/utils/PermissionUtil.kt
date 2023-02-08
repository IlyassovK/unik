package com.example.students.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat


object PermissionUtil {


    fun checkLocationPermissions(context: Context): Boolean {
        return checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,
            context) && checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, context)
    }

    fun checkPermission(permission: String, context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(context,
            permission) == PackageManager.PERMISSION_GRANTED
    }
}