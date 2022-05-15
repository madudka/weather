package com.madudka.weather.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.madudka.weather.R
import com.madudka.weather.databinding.FragmentDayInfoBinding
import com.madudka.weather.view.*

class DayInfoFragment : DayBaseFragment<DayModel>() {

    //private lateinit var viewContext: Context

    private lateinit var binding : FragmentDayInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener{
            fragManager.popBackStack()
        }

        //viewContext = view.context

        updateView()
    }

    override fun updateView() {
        listData?.apply {
            binding.dayDateTv.text = dt.toDateFormat(FORMAT_DAY_MONTH_NAME)
            binding.dayTempTv.text = temp.getAvg().toDegree()
            binding.dayIconImg.setImageResource(weather[0].icon.provideIcon())

            binding.dayTempMorn.text = temp.morn.toDegree()
            binding.dayTempDay.text = temp.day.toDegree()
            binding.dayTempEven.text = temp.eve.toDegree()
            binding.dayTempNight.text = temp.night.toDegree()
            binding.dayTempMornFeel.text = feels_like.morn.toDegree()
            binding.dayTempDayFeel.text = feels_like.day.toDegree()
            binding.dayTempEvenFeel.text = feels_like.eve.toDegree()
            binding.dayTempNightFeel.text = feels_like.night.toDegree()

            binding.humidityTv.text = humidity.toExtra("%")
            val pres = SettingsHolder.pres
            binding.pressureTv.text = getString(pres.unitStringRes, pres.getValue(pressure.toDouble()))
            val ws = SettingsHolder.ws
            binding.windSpeedTv.text = getString(ws.unitStringRes, ws.getValue(wind_speed))
            binding.windDirectionTv.text = wind_deg.toString()
            binding.sunriseTv.text = sunrise.toDateFormat(FORMAT_HOUR_MINUTE)
            binding.sunsetTv.text = sunset.toDateFormat(FORMAT_HOUR_MINUTE)

        }
    }
}