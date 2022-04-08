package com.madudka.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.*
import com.google.android.gms.security.ProviderInstaller
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.madudka.weather.databinding.ActivityMainBinding
import com.madudka.weather.view.adapter.MainDayListAdapter
import com.madudka.weather.view.adapter.MainHourListAdapter
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext


const val GEO_LOC_REQ_SUCCESS_CODE = 1
const val ERROR_DIALOG_REQUEST_CODE = 1
const val TAG = "GEO"

class MainActivity : AppCompatActivity(), ProviderInstaller.ProviderInstallListener {

    private lateinit var activityMainBinding: ActivityMainBinding

    private val fusedLocProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locReq by lazy { createLocReq() }
    private lateinit var loc: Location


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)

        ProviderInstaller.installIfNeededAsync(this, this)

        checkPermissions()

        init()

        activityMainBinding.mainHourList.apply {
            adapter = MainHourListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        activityMainBinding.mainDayList.apply {
            adapter = MainDayListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        fusedLocProviderClient.requestLocationUpdates(locReq, locCallback, Looper.getMainLooper())

    }

    private fun init() {
        activityMainBinding.locationTv.text = "Mosocow"
        activityMainBinding.dateTv.text = "29 april"
        activityMainBinding.mainTemp.text = "+15\u00B0"
        activityMainBinding.mainImg.setImageResource(R.mipmap.cloud1x)
        activityMainBinding.minTempTv.text = "+10"
        activityMainBinding.maxTempTv.text = "+19"
        activityMainBinding.mainWeatherIcon.setImageResource(R.drawable.ic_sunny)
        activityMainBinding.mainWeatherTv.text = "Clear"
        activityMainBinding.mainWindTv.text = "5 m/s"
        activityMainBinding.mainHumidityTv.text = "80 %"
        activityMainBinding.mainPressureTv.text = "750 mm"
        activityMainBinding.mainSunriseTv.text = "06:00"
        activityMainBinding.mainSunsetTv.text = "23:00"
    }

    private fun createLocReq(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private var locCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "onLocationResult: ${locationResult.locations.size}")
            for (l in locationResult.locations) {
                loc = l
                Log.d(TAG, "location: lat ${l.latitude}, lon ${l.longitude}")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: $requestCode")
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            MaterialAlertDialogBuilder(this)
                .setTitle("Приложению необходимы геоданные")
                .setMessage("Пожалуйста разрешите получать геоданные")
                .setPositiveButton("Good") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        GEO_LOC_REQ_SUCCESS_CODE
                    )
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        GEO_LOC_REQ_SUCCESS_CODE
                    )
                }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
        GoogleApiAvailability.getInstance().apply {
            if (isUserResolvableError(errorCode)) {
                // Recoverable error. Show a dialog prompting the user to
                // install/update/enable Google Play services.
                showErrorDialogFragment(this@MainActivity, errorCode, ERROR_DIALOG_REQUEST_CODE) {
                    // The user chose not to take the recovery action
                    onProviderInstallerNotAvailable()
                }
            } else {
                onProviderInstallerNotAvailable()
            }
        }
    }

    private fun onProviderInstallerNotAvailable() {
        // This is reached if the provider cannot be updated for some reason.
        // App should consider all HTTP communication to be vulnerable, and take
        // appropriate action.
    }

    override fun onProviderInstalled() {
    }

}