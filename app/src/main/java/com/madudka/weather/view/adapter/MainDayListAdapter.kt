package com.madudka.weather.view.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.madudka.weather.R
import com.madudka.weather.databinding.ItemMainDayBinding
import com.madudka.weather.model.DayModel
import com.madudka.weather.view.*

class MainDayListAdapter : BaseAdapter<DayModel>() {

    lateinit var clickListener: DayItemClick

    interface DayItemClick{
        fun showDetails(data: DayModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val viewBinding = ItemMainDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(viewBinding)
    }

    inner class DayViewHolder(private val viewBinding: ItemMainDayBinding) : BaseViewHolder(viewBinding.root){

        override fun bindView(position: Int) {

            val itemData = listData[position]

            val defTextColor = viewBinding.itemDayDateTv.textColors
            if (position == 0){
                viewBinding.itemDayDateTv.setTextColor(
                    ContextCompat.getColor(viewBinding.itemDayDateTv.context, R.color.sky))
            } else {
                viewBinding.itemDayDateTv.setTextColor(defTextColor)
            }

            viewBinding.itemDayContainer.setOnClickListener {
                clickListener.showDetails(itemData)
            }

            if (listData.isNotEmpty()) {
                itemData.apply {
                    val dateDay = dt.toDateFormat(FORMAT_DAY_WEEK_NAME)
                     viewBinding.itemDayDateTv.text = if (dateDay.startsWith("0",true))
                         dateDay.removePrefix("0") else dateDay

                    viewBinding.itemDayPopTv.text = pop.toExtra("%")
                    viewBinding.itemDayImg.setImageResource(weather[0].icon.provideIcon())
                    viewBinding.itemDayMaxTv.text = temp.max.toDegree()
                    viewBinding.itemDayMinTv.text = temp.min.toDegree()
                }
            }
        }
    }
}