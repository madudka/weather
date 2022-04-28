package com.madudka.weather.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<D> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    private val _listData by lazy { mutableListOf<D>() }
    protected val listData : List<D> = _listData

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount() = _listData.size

    fun updateData(data: List<D>) {
        if (_listData.isNotEmpty() || data.isEmpty()) _listData.clear()
        _listData.addAll(data)
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bindView(position: Int)
    }
}