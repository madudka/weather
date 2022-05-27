package com.madudka.weather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.madudka.weather.databinding.ItemMainHourBinding
import com.madudka.weather.model.HourModel
import com.madudka.weather.view.*


class MainHourListAdapter : BaseAdapter<HourModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val viewBinding = ItemMainHourBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return HourViewHolder(viewBinding)
    }

    inner class HourViewHolder(private val viewBinding: ItemMainHourBinding) : BaseViewHolder(viewBinding.root) {

        override fun bindView(position: Int) {
            listData[position].apply {
                viewBinding.itemHourTimeTv.text = dt.toDateFormat(FORMAT_HOUR_MINUTE)
                viewBinding.itemHourTempTv.text =  temp.toDegree()
                viewBinding.itemHourImg.setImageResource(weather[0].icon.provideIcon())
                viewBinding.itemHourPopTv.text = pop.toExtra("%")
            }
        }
    }
}