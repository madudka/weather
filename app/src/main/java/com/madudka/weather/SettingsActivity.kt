package com.madudka.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.button.MaterialButtonToggleGroup
import com.madudka.weather.databinding.ActivitySettingsBinding
import com.madudka.weather.view.SettingsHolder

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.settingsToolbar.setNavigationOnClickListener { onBackPressed() }

        setPrefSettings()

        listOf(binding.tempGroup, binding.windSpeedGroup, binding.presGroup).forEach {
            it.addOnButtonCheckedListener (ToggleButtonClickListener)
        }

        AdYandexBanner().loadBanner(binding)
        AdYandexInterstitial().loadInterstitial(applicationContext, binding)

    }

    override fun onDestroy() {
        super.onDestroy()
        SettingsHolder.onDestroy()
        AdYandexInterstitial.onInterstitialAdDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_right_out)
    }

    private fun setPrefSettings(){
        binding.tempGroup.check(SettingsHolder.temp.checkedViewId)
        binding.windSpeedGroup.check(SettingsHolder.ws.checkedViewId)
        binding.presGroup.check(SettingsHolder.pres.checkedViewId)
    }

    private object ToggleButtonClickListener : MaterialButtonToggleGroup.OnButtonCheckedListener{
        override fun onButtonChecked(
            group: MaterialButtonToggleGroup?,
            checkedId: Int,
            isChecked: Boolean
        ) {
            when (checkedId to isChecked) {
                R.id.temp_unit_c to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_C
                R.id.temp_unit_f to true -> SettingsHolder.temp = SettingsHolder.Setting.TEMP_F
                R.id.wind_speed_unit_ms to true -> SettingsHolder.ws = SettingsHolder.Setting.WS_MS
                R.id.wind_speed_unit_kmh to true -> SettingsHolder.ws = SettingsHolder.Setting.WS_KMH
                R.id.pres_unit_mmHg to true -> SettingsHolder.pres = SettingsHolder.Setting.PRES_MMHG
                R.id.pres_unit_hpa to true -> SettingsHolder.pres = SettingsHolder.Setting.PRES_HPA
            }
        }


    }

}