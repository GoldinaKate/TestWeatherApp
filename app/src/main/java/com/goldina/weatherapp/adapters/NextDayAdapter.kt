package com.goldina.weatherapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goldina.weatherapp.databinding.NextdayItemBinding
import com.goldina.weatherapp.model.WeatherNextDay

class NextDayAdapter(private val items: List<WeatherNextDay>, context: Context) :
    RecyclerView.Adapter<NextDayAdapter.ViewHolder>() {

    private lateinit var adapter: HourlyAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NextdayItemBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(private val binding: NextdayItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(weatherDay: WeatherNextDay) = with(itemView) {
            binding.tvMaxTemp.text = "↑ ${weatherDay.tempMax.toInt()}°"
            binding.tvMinTemp.text = "↓ ${weatherDay.tempMin.toInt()}°"
            binding.tvDate.text = weatherDay.date
            binding.tvDate.text = "${weatherDay.dayName}, ${weatherDay.date}"
            adapter = HourlyAdapter(weatherDay.listHourly, context)
            binding.rvWeatherHourly.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            binding.rvWeatherHourly.setHasFixedSize(true)
            binding.rvWeatherHourly.adapter = adapter
        }

    }
}