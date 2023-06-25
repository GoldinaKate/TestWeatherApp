package com.goldina.weatherapp.activity

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.goldina.weatherapp.R
import com.goldina.weatherapp.adapters.NextDayAdapter
import com.goldina.weatherapp.databinding.ActivityForecastBinding
import com.goldina.weatherapp.model.DayWeather
import com.goldina.weatherapp.model.WeatherNextDay
import com.goldina.weatherapp.model.response.ResponseNextDay
import com.goldina.weatherapp.receiver.NetworkChangeReceiver
import com.goldina.weatherapp.utils.Constants
import com.goldina.weatherapp.utils.DataState
import com.goldina.weatherapp.utils.network.Network
import com.goldina.weatherapp.utils.network.OnNetworkListener
import com.goldina.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.TimeZone

@AndroidEntryPoint
class ForecastActivity : AppCompatActivity(), OnNetworkListener {

    private var networkReceiver: NetworkChangeReceiver? = null
    private lateinit var binding: ActivityForecastBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var adapter: NextDayAdapter
    private var listWeatherNextDays = mutableListOf<WeatherNextDay>()
    private lateinit var snack: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)
        snack = Snackbar.make(
            findViewById(android.R.id.content),
            "",
            Snackbar.LENGTH_INDEFINITE
        )


        if(Network.isOnline(this)) {
            loadDataWeather()
        }else{

            showSnackBar(getString(R.string.no_internet_connection))
        }
        networkReceiver = NetworkChangeReceiver(this)
        refreshLayout()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setData(nextDay: ResponseNextDay) {
        val dayNameFormat = SimpleDateFormat("EEEE")
        val dateFormat = SimpleDateFormat("dd MMM")
        val timeCheck = SimpleDateFormat("HH:mm")
        timeCheck.timeZone = TimeZone.getTimeZone("UTC")
        dateFormat.timeZone= TimeZone.getTimeZone("UTC")
        dayNameFormat.timeZone= TimeZone.getTimeZone("UTС")
        var tempMinDay = 100.0
        var tempMaxDay = -100.0
        var listHourlyOneDay = mutableListOf<DayWeather>()
        binding.refreshLayout.isRefreshing=false
        nextDay.list.forEach{ item->
                if(item.main.temp_min<tempMinDay)
                    tempMinDay=item.main.temp_min
                if(item.main.temp_max>tempMaxDay)
                    tempMaxDay=item.main.temp_max
                listHourlyOneDay.add(item)
                if(timeCheck.format(item.dt*1000)=="21:00"){
                    listWeatherNextDays.add(
                        WeatherNextDay(
                            dateFormat.format(item.dt*1000),
                            dayNameFormat.format(item.dt*1000),
                            tempMinDay,
                            tempMaxDay,
                            listHourlyOneDay
                        )
                    )
                    tempMinDay = 100.0
                    tempMaxDay = -100.0
                    listHourlyOneDay=mutableListOf()
                }
            }

            binding.apply {
                adapter = NextDayAdapter(listWeatherNextDays,this@ForecastActivity)
                binding.rvWeatherDaily.layoutManager = LinearLayoutManager(baseContext)
                binding.rvWeatherDaily.setHasFixedSize(true)
                binding.rvWeatherDaily.adapter = adapter
            }
            listWeatherNextDays = mutableListOf()

    }

    private fun loadDataWeather() {
        viewModel.getNextWeatherData(
            Constants.LAT,
            Constants.LON,
            Constants.API_KEY,
            Constants.UNITS)
        viewModel.respNextWeather.observe(this) {response->
            run{
                when(response){
                    is DataState.Success<Any> -> {
                        displayProgressBar(false)
                        setData(response.data as ResponseNextDay)
                    }
                    is DataState.Error -> {
                        displayProgressBar(false)
                        displayError(response.exception.message)
                    }

                    is DataState.Loading -> {
                        displayProgressBar(true)
                    }
                    is DataState.OtherError -> {
                        displayProgressBar(false)
                        displayError(response.error)
                    }
                }
            }
        }
    }

    private fun displayProgressBar(isDisplay: Boolean){
        if(isDisplay){
            binding.progressBar.visibility = View.VISIBLE
            binding.rvWeatherDaily.visibility = View.INVISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.rvWeatherDaily.visibility = View.VISIBLE
        }
    }

    private fun displayError(message: String?){
        if (message != null){
            showSnackBar( message)
        }else{
            showSnackBar(getString(R.string.issues_message))
        }
    }

    private fun refreshLayout() {
        binding.refreshLayout.setColorSchemeColors(resources.getColor(R.color.colorPrimary)
            ,resources.getColor(R.color.colorPrimaryDark))
        binding.refreshLayout.setOnRefreshListener {
            loadDataWeather()
        }
    }

    private fun showSnackBar(error: String) {
        snack.setAction("RETRY") {
            snack.dismiss()
            loadDataWeather()
        }
        snack.setText(error)
        snack.setActionTextColor(ContextCompat.getColor(this, R.color.white))
        snack.view.setBackgroundColor(ContextCompat.getColor(this, androidx.appcompat.R.color.material_blue_grey_900))
        snack.show()
    }

    override fun onNetworkConnected() {
        snack.dismiss()
        loadDataWeather()
    }

    override fun onNetworkDisconnected() {
        showSnackBar(getString(R.string.no_internet_connection))
    }
    override fun onStart() {
        super.onStart()
        try {
            registerNetworkBroadcastForNougat()
        } catch (e: Exception) {
            Log.d("Network", "onStart: " + "already registered")
        }
    }

    private fun registerNetworkBroadcastForNougat() {
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        try {
            unregisterReceiver(networkReceiver)
        } catch (e: Exception) {
            Log.d("Network", "onStop: " + "already unregistered")
        }
        super.onStop()
    }


}