package com.madudka.weather

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.GoogleApiAvailability

import com.google.android.gms.location.*
import com.google.android.gms.security.ProviderInstaller
import com.madudka.weather.databinding.ActivityMainBinding
import com.madudka.weather.model.DayModel
import com.madudka.weather.model.HourModel
import com.madudka.weather.model.WeatherDataModel
import com.madudka.weather.presenter.MainPresenter
import com.madudka.weather.view.*
import com.madudka.weather.view.adapter.MainDayListAdapter
import com.madudka.weather.view.adapter.MainHourListAdapter
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

const val COORDINATES = "COORDINATES"
const val ERROR_DIALOG_REQUEST_CODE = 1
const val TAG = "GEO"
//ProviderInstaller.ProviderInstallListener
class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter { MainPresenter() }

    private lateinit var binding: ActivityMainBinding

    private val fusedLocProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locReq by lazy { createLocReq() }
    private lateinit var loc: Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //ProviderInstaller.installIfNeededAsync(this, this)

        init()

        if (!intent.hasExtra(COORDINATES)) {
            fusedLocProviderClient.requestLocationUpdates(locReq, locCallback, Looper.getMainLooper())
        } else {
            val coordinatesExtra = intent.extras!!.getBundle(COORDINATES)!!
            val locExtra = Location("")
            locExtra.latitude = coordinatesExtra.getString("lat")!!.toDouble()
            locExtra.longitude = coordinatesExtra.getString("lon")!!.toDouble()
            loc = locExtra
            mainPresenter.refresh(lat = loc.latitude.toString(), lon = loc.longitude.toString())
        }

        binding.locationBtn.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left_in, R.anim.fade_out)
        }

        binding.settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_right_in, R.anim.fade_out)
        }

        binding.mainHourList.apply {
            adapter = MainHourListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        binding.mainDayList.apply {
            adapter = MainDayListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        mainPresenter.enable()

        //fusedLocProviderClient.requestLocationUpdates(locReq, locCallback, Looper.getMainLooper())

    }

    private fun init() {
        binding.locationTv.text = "Mosocow"
        binding.dateTv.text = "29 april"
        binding.mainTemp.text = "15\u00B0"
        binding.mainImg.setImageResource(R.mipmap.cloud1x)
        binding.minTempTv.text = "10"
        binding.maxTempTv.text = "19"
        binding.mainWeatherIcon.setImageResource(R.drawable.ic_sunny)
        binding.mainWeatherTv.text = "Clear"
        binding.mainWindTv.text = "5 m/s"
        binding.mainHumidityTv.text = "80 %"
        binding.mainPressureTv.text = "750 mm"
        binding.mainSunriseTv.text = "06:00"
        binding.mainSunsetTv.text = "23:00"
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
                mainPresenter.refresh(l.latitude.toString(), l.longitude.toString())
                Log.d(TAG, "location: lat ${l.latitude}, lon ${l.longitude}")
            }
        }
    }



//    override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
//        GoogleApiAvailability.getInstance().apply {
//            if (isUserResolvableError(errorCode)) {
//                // Recoverable error. Show a dialog prompting the user to
//                // install/update/enable Google Play services.
//                showErrorDialogFragment(this@MainActivity, errorCode, ERROR_DIALOG_REQUEST_CODE) {
//                    // The user chose not to take the recovery action
//                    onProviderInstallerNotAvailable()
//                }
//            } else {
//                onProviderInstallerNotAvailable()
//            }
//        }
//    }
//
//    private fun onProviderInstallerNotAvailable() {
//        // This is reached if the provider cannot be updated for some reason.
//        // App should consider all HTTP communication to be vulnerable, and take
//        // appropriate action.
//    }
//
//    override fun onProviderInstalled() {
//    }

    override fun showLocation(data: String) {
        binding.locationTv.text = data
    }

    override fun showCurrentData(data: WeatherDataModel) {
        data.apply {
            binding.dateTv.text = current.dt.toDateFormat(FORMAT_DAY_MONTH_NAME)
            binding.mainTemp.text = current.temp.toDegree()
            binding.mainImg.setImageResource(current.weather[0].icon.provideImage())
            daily[0].temp.apply {
                binding.minTempTv.text = min.toDegree()
                binding.maxTempTv.text = max.toDegree()
            }

            val ws = SettingsHolder.ws
            binding.mainWindTv.text = getString(ws.unitStringRes, ws.getValue(current.wind_speed))

            val pres = SettingsHolder.pres
            binding.mainPressureTv.text = getString(pres.unitStringRes, pres.getValue(current.pressure.toDouble()))

            binding.mainWeatherIcon.setImageResource(current.weather[0].icon.provideIcon())
            binding.mainWeatherTv.text = current.weather[0].description
            binding.mainHumidityTv.text = current.humidity.toExtra("%")

            binding.mainSunriseTv.text = current.sunrise.toDateFormat(FORMAT_HOUR_MINUTE)
            binding.mainSunsetTv.text = current.sunset.toDateFormat(FORMAT_HOUR_MINUTE)
        }
    }

    override fun showHourData(data: List<HourModel>) {
        (binding.mainHourList.adapter as MainHourListAdapter).updateData(data)
    }

    override fun showDayData(data: List<DayModel>) {
        (binding.mainDayList.adapter as MainDayListAdapter).updateData(data)
    }

    override fun showError(error: Throwable) {

    }

    override fun setLoading(flag: Boolean) {

    }

}