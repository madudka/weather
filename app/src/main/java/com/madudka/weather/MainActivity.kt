package com.madudka.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Point
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.madudka.weather.databinding.ActivityMainBinding
import com.madudka.weather.model.DayModel
import com.madudka.weather.model.HourModel
import com.madudka.weather.model.WeatherDataModel
import com.madudka.weather.presenter.MainPresenter
import com.madudka.weather.view.*
import com.madudka.weather.view.adapter.MainHourListAdapter
import io.reactivex.rxjava3.subjects.BehaviorSubject
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import kotlin.math.roundToInt

const val COORDINATES = "COORDINATES"
const val TAG = "GEO"

class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter { MainPresenter() }

    private lateinit var binding: ActivityMainBinding

    private val tokenSource = CancellationTokenSource()
    private val fusedLocProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locReq by lazy { createLocReq() }
    private lateinit var loc: Location
    private val locEmitter : BehaviorSubject<Location> = BehaviorSubject.create()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        locEmitter
            .doAfterNext { fusedLocProviderClient.removeLocationUpdates(locCallback) }
            .subscribe { l ->
                mainPresenter.refresh(l.latitude.toString(), l.longitude.toString()) }

        initBottomSheet()
        init()
        initSwipeRefresh()

        binding.swipeLayout.isRefreshing = true

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, DayListFragment(), DayListFragment::class.simpleName)
            .commit()

        if (!intent.hasExtra(COORDINATES)) {
            checkGeoAvailable()
            getGeoData()
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

        binding.findLocBtn.setOnClickListener {
            fusedLocProviderClient.requestLocationUpdates(locReq, locCallback, Looper.getMainLooper())
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
//MOVED TO FRAGMENT
//        binding.mainDayList.apply {
//            adapter = MainDayListAdapter()
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            setHasFixedSize(true)
//        }

        mainPresenter.enable()

        //fusedLocProviderClient.requestLocationUpdates(locReq, locCallback, Looper.getMainLooper())

    }

    override fun onResume() {
        super.onResume()
        if (this::loc.isInitialized) mainPresenter.refresh(loc.latitude.toString(), loc.longitude.toString())
    }

    override fun onStop() {
        super.onStop()
        fusedLocProviderClient.removeLocationUpdates(locCallback)
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
            interval = 600000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private var locCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "onLocationResult: ${locationResult.locations.size}")
            for (l in locationResult.locations) {
                loc = l
                //mainPresenter.refresh(l.latitude.toString(), l.longitude.toString())
                locEmitter.onNext(loc)
                Log.d(TAG, "location: lat ${l.latitude}, lon ${l.longitude}")
            }
        }
    }

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
            binding.mainWeatherTv.text = current.weather[0].description.replaceFirstChar(Char::titlecase)
            binding.mainHumidityTv.text = current.humidity.toExtra("%")

            binding.mainSunriseTv.text = current.sunrise.toDateFormat(FORMAT_HOUR_MINUTE)
            binding.mainSunsetTv.text = current.sunset.toDateFormat(FORMAT_HOUR_MINUTE)
        }
    }

    override fun showHourData(data: List<HourModel>) {
        (binding.mainHourList.adapter as MainHourListAdapter).updateData(data)
    }

    override fun showDayData(data: List<DayModel>) {
        (supportFragmentManager.findFragmentByTag(DayListFragment::class.simpleName) as DayListFragment).setData(data)
    }

    override fun showError(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
    }

    override fun setLoading(flag: Boolean) {
        binding.swipeLayout.isRefreshing = flag
    }

    private fun initBottomSheet(){
        binding.bottomSheet.isNestedScrollingEnabled = true

        val width: Int
        val height: Int
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val windowInsets: WindowInsets = windowMetrics.windowInsets

            val insets = windowInsets.getInsetsIgnoringVisibility(
                WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout())
            val insetsWidth = insets.right + insets.left
            val insetsHeight = insets.top + insets.bottom

            val b = windowMetrics.bounds
            width = b.width() - insetsWidth
            height = b.height() - insetsHeight
        } else {
            val size = Point()
            val display = wm.defaultDisplay // deprecated in API 30
            display?.getSize(size) // deprecated in API 30
            width = size.x
            height = size.y
        }

        binding.bottomSheetLayout.layoutParams = CoordinatorLayout.LayoutParams(width, (height * 0.6).roundToInt())
    }

    private fun initSwipeRefresh(){
        binding.swipeLayout.apply {
            setColorSchemeResources(R.color.sky)
            setProgressViewEndTarget(false, 280)
            setOnRefreshListener {
                mainPresenter.refresh(loc.latitude.toString(), loc.longitude.toString())
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getGeoData(){
        fusedLocProviderClient
            .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, tokenSource.token)
            .addOnSuccessListener {
                if (it != null){
                    loc = it
                    //mainPresenter.refresh(loc.latitude.toString(), loc.longitude.toString())
                    locEmitter.onNext(loc)
                } else{
                    showError(Exception("Geo data is not available"))
                }
            }
    }

    private fun checkGeoAvailable(){
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locReq)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnFailureListener { ex ->
            if (ex is ResolvableApiException){
                try {
                    ex.startResolutionForResult(this, 100)
                } catch (sendIntentEx : IntentSender.SendIntentException){

                }
            }
        }
    }

}