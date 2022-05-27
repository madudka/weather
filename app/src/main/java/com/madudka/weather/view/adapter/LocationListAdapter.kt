package com.madudka.weather.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.madudka.weather.R
import com.madudka.weather.databinding.ItemLocationBinding
import com.madudka.weather.model.GeoCodeModel
import java.util.*

class LocationListAdapter : BaseAdapter<GeoCodeModel>() {

    lateinit var clickListener: SearchItemClickListener

    interface SearchItemClickListener {
        fun removeFromFavorite(item: GeoCodeModel)

        fun addToFavorite(item: GeoCodeModel)

        fun showWeatherIn(item: GeoCodeModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val viewBinding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(viewBinding)
    }

    inner class LocationViewHolder(private val viewBinding: ItemLocationBinding) : BaseViewHolder(viewBinding.root){

        override fun bindView(position: Int) {

            viewBinding.locationCard.setOnClickListener {
                clickListener.showWeatherIn(listData[position])
            }

            viewBinding.favoriteBtn.setOnClickListener {
                val item = listData[position]
                when ((it as MaterialButton).isChecked) {
                    true -> {
                        item.isFavorite = true
                        clickListener.addToFavorite(item)
                    }
                    false -> {
                        item.isFavorite = false
                        clickListener.removeFromFavorite(item)
                    }
                }
            }

            listData[position].apply {
                viewBinding.state.text = if (state.isNullOrEmpty()) ""
                    else itemView.context.getString(R.string.state, state)
                viewBinding.city.text = when (Locale.getDefault().displayLanguage){
                    "русский" -> if (local_names != null) local_names.ru ?: name else name
                    "english" -> if (local_names != null) local_names.en ?: name else name
                    else -> name
                }
                viewBinding.country.text = Locale("", country).displayName
                viewBinding.favoriteBtn.isChecked = isFavorite
            }
        }

    }

}