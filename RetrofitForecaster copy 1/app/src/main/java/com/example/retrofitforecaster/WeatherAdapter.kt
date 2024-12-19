package com.example.retrofitforecaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter : ListAdapter<WeatherItem, RecyclerView.ViewHolder>(WeatherDiffCallback()) {

    companion object {
        const val TYPE_HOT = 1
        const val TYPE_COLD = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).main.temp >= 0) TYPE_HOT else TYPE_COLD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HOT -> HotViewHolder(inflater.inflate(R.layout.item_hot, parent, false))
            else -> ColdViewHolder(inflater.inflate(R.layout.item_cold, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is HotViewHolder -> holder.bind(item)
            is ColdViewHolder -> holder.bind(item)
        }
    }

    class HotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tempText: TextView = view.findViewById(R.id.tv_temp)
        fun bind(item: WeatherItem) {
            tempText.text = "${item.main.temp}°C - ${item.weather[0].description}"
        }
    }

    class ColdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tempText: TextView = view.findViewById(R.id.tv_temp)
        fun bind(item: WeatherItem) {
            tempText.text = "${item.main.temp}°C - ${item.weather[0].description}"
        }
    }
}

class WeatherDiffCallback : DiffUtil.ItemCallback<WeatherItem>() {
    override fun areItemsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: WeatherItem, newItem: WeatherItem): Boolean {
        return oldItem == newItem
    }
}