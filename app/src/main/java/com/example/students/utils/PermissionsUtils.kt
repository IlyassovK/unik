package com.example.students.utils

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.students.R
import timber.log.Timber


object PermissionsUtils {


    fun allowPermission(context: Context, permission: String?): Boolean {
        return permission?.let { context.checkSelfPermission(it) } == PackageManager.PERMISSION_GRANTED
    }

    fun checkLocationPermissions(context: Context): Boolean {
        return allowPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun checkCameraPermission(context: Context): Boolean {
        Timber.d("TEMP ARA: checkCameraPermission() - " +
                "${allowPermission(context, android.Manifest.permission.CAMERA)}")

        return allowPermission(context, android.Manifest.permission.CAMERA)
    }

    fun checkReadContactsPermission(context: Context): Boolean {
        return allowPermission(context, android.Manifest.permission.READ_CONTACTS)
    }

    fun getPermissionsArray(activity: Activity): Array<String> {
        try {
            val info: PackageInfo =
                activity.let {
                    it.packageManager
                        .getPackageInfo(it.packageName, PackageManager.GET_PERMISSIONS)
                }
            info.requestedPermissions?.let { return it }
        } catch (e: Exception) {
        }
        return arrayOf()

    }

    fun go2AppSettings(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity.getPackageName(), null)
        )
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }

    fun requestPermissionsFromActivity(
        activity: Activity,
        permission: Array<String>?,
        requestCode: Int
    ) {
        permission?.let { activity.requestPermissions(it, requestCode) }
    }

    fun requestPermissionsFromFragment(
        fragment: Fragment,
        permission: Array<String?>?,
        requestCode: Int
    ) {
        permission?.let { fragment.requestPermissions(it, requestCode) }
    }

    fun checkComplexLocationPermissions(context: Context): Boolean {
        return isSystemLocationServiceEnabled(context) && isSystemLocationEnabled(context)
    }

    fun checkSystemLocationServiceWithDialog(
        context: Context,
        acceptListener: DialogInterface.OnClickListener?,
        cancelListener: DialogInterface.OnClickListener?
    ) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as (LocationManager)
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (ex: Exception) {
            // TODO:  reaction
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (ex: Exception) {
            // TODO:  reaction
        }
        if (!gpsEnabled || !networkEnabled) {
            val dialog = AlertDialog.Builder(context)
                .setMessage(R.string.permissions__location__dialog_title)
                .setPositiveButton(R.string.permissions__location__dialog_accept)
                { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton(R.string.permissions__location__dialog_cancel, cancelListener)
                .create()
            dialog.show();
        }
    }

    fun checkSystemLocationServiceWithDialogInLocation(
        context: Context,
        acceptListener: DialogInterface.OnClickListener?,
        cancelListener: DialogInterface.OnClickListener?
    ) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as (LocationManager)
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (ex: Exception) {
            // TODO:  reaction
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (ex: Exception) {
            // TODO:  reaction
        }
        if (!gpsEnabled || !networkEnabled) {
            val dialog = AlertDialog.Builder(context)
                .setMessage(R.string.permissions__location__dialog_title)
                .setPositiveButton(R.string.permissions__location__dialog_accept)
                { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton(R.string.permissions__location__dialog_cancel, cancelListener)
                .create()
            dialog.show();
        }
    }

    fun showLocationDeniedDialog(
        context: Context,
        acceptListener: DialogInterface.OnClickListener?,
        cancelListener: DialogInterface.OnClickListener?
    ) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri

        val dialog = AlertDialog.Builder(context)
            .setMessage(R.string.permissions__location__dialog_title)
            .setPositiveButton(R.string.permissions__location__dialog_accept)
            { _, _ -> context.startActivity(intent) }
            .setNegativeButton(R.string.permissions__location__dialog_cancel, cancelListener)
            .create()
        dialog.show();
    }

    fun isSystemLocationServiceEnabled(
        context: Context
    ): Boolean {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as (LocationManager)
        var gpsEnabled = false
        var networkEnabled = false
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (ex: Exception) {
            // TODO:  reaction
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (ex: Exception) {
            // TODO:  reaction
        }
        return gpsEnabled && networkEnabled
    }

    fun isSystemLocationEnabled(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}