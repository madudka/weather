package com.madudka.weather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.madudka.weather.databinding.ItemMainDayBinding
import com.madudka.weather.model.DayModel
import com.madudka.weather.view.*

class MainDayListAdapter : BaseAdapter<DayModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_main_day, parent, false)
        val viewBinding = ItemMainDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(viewBinding)
    }

    inner class DayViewHolder(private val viewBinding: ItemMainDayBinding) : BaseViewHolder(viewBinding.root){

        override fun bindView(position: Int) {
            listData[position].apply {
                viewBinding.itemDayDateTv.text = dt.toDateFormat(FORMAT_DAY_WEEK_NAME)
                viewBinding.itemDayPopTv.text = pop.toExtra("%")
                viewBinding.itemDayImg.setImageResource(weather[0].icon.provideIcon())
                viewBinding.itemDayMaxTv.text = temp.max.toCelsius()
                viewBinding.itemDayMinTv.text = temp.min.toCelsius()
            }
        }

    }
}