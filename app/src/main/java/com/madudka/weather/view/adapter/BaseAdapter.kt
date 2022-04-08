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

    fun updateData() {
        if (_listData.isNotEmpty() || listData.isEmpty()) _listData.clear()
        _listData.addAll(listData)
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        abstract fun bindView(position: Int)
    }
}