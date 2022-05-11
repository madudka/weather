package com.madudka.weather

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.madudka.weather.databinding.ActivityLocationBinding
import com.madudka.weather.model.GeoCodeModel
import com.madudka.weather.presenter.LocationPresenter
import com.madudka.weather.view.LocationView
import com.madudka.weather.view.adapter.LocationListAdapter
import com.madudka.weather.view.createObservable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.util.concurrent.TimeUnit

class LocationActivity : MvpAppCompatActivity(), LocationView {

    private lateinit var binding : ActivityLocationBinding

    private val presenter by moxyPresenter { LocationPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_location)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        presenter.enable()
        presenter.getFavoriteList()

        binding.searchLocInput.createObservable()
            .doOnNext { setLoading(true) }
            .debounce(700, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                if (!it.isNullOrEmpty()) presenter.search(it)
            }

        initLocationList(binding.searchResult)
        initLocationList(binding.searchFavorite)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_left_out)
    }

    override fun fillResultList(data: List<GeoCodeModel>) {
        (binding.searchResult.adapter as LocationListAdapter).updateData(data)
    }

    override fun fillFavoriteList(data: List<GeoCodeModel>) {
        (binding.searchFavorite.adapter as LocationListAdapter).updateData(data)
    }

    override fun setLoading(flag: Boolean) {
        binding.searchProgress.isActivated = flag
        binding.searchProgress.visibility = if (flag) View.VISIBLE else View.GONE
    }

    private fun initLocationList(rv: RecyclerView){
        val locationAdapter = LocationListAdapter()
        locationAdapter.clickListener = searchItemClickListener
        rv.apply {
            adapter = locationAdapter
            layoutManager = object : LinearLayoutManager(this@LocationActivity, VERTICAL, false){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            setHasFixedSize(true)
        }
    }

    private val searchItemClickListener = object : LocationListAdapter.SearchItemClickListener{

        override fun removeFromFavorite(item: GeoCodeModel){
            presenter.remove(item)
        }

        override fun addToFavorite(item: GeoCodeModel){
            presenter.save(item)
        }

        override fun showWeatherIn(item: GeoCodeModel){
            val intent = Intent(this@LocationActivity, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putString("lat", item.lat.toString())
            bundle.putString("lon", item.lon.toString())
            intent.putExtra(COORDINATES, bundle)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.slide_left_out)
        }

    }

}