package com.madudka.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.madudka.weather.databinding.FragmentDayListBinding
import com.madudka.weather.model.DayModel
import com.madudka.weather.view.adapter.MainDayListAdapter

class DayListFragment : DayBaseFragment<List<DayModel>>() {

    private lateinit var binding: FragmentDayListBinding
    private val dayListAdapter = MainDayListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_left)
        enterTransition = inflater.inflateTransition(R.transition.slide_left)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentDayList.apply {
            adapter = dayListAdapter
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            dayListAdapter.clickListener = clickListener
        }

        listData?.let {
            updateView()
        }
    }

    override fun updateView() {
        dayListAdapter.updateData(listData!!)
    }

    private val clickListener = object : MainDayListAdapter.DayItemClick{
        override fun showDetails(data: DayModel) {
            val fragment = DayInfoFragment()
            fragment.setData(data)
            fragManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
}