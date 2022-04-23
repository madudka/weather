package com.madudka.weather.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.madudka.weather.R
import com.madudka.weather.model.DayModel

class MainDayListAdapter : BaseAdapter<DayModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_day, parent, false)
        return DayViewHolder(view)
    }

    inner class DayViewHolder(view : View) : BaseViewHolder(view){
        override fun bindView(position: Int) {

        }

    }
}