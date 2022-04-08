package com.madudka.weather.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textview.MaterialTextView
import com.madudka.weather.R
import com.madudka.weather.model.HourModel


class MainHourListAdapter : BaseAdapter<HourModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_hour, parent, false)
        return HourViewHolder(view)
    }

    inner class HourViewHolder(view : View) : BaseViewHolder(view) {

        lateinit var time : MaterialTextView
        lateinit var temper : MaterialTextView
        lateinit var icon: MaterialTextView
        lateinit var humidity : MaterialTextView

        override fun bindView(position: Int) {

        }
    }
}