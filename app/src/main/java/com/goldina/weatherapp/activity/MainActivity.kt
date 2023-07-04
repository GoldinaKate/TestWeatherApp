package com.goldina.weatherapp.activity

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.goldina.weatherapp.R
import com.goldina.weatherapp.databinding.ActivityMainBinding
import com.goldina.weatherapp.model.response.ResponseCurrentDate
import com.goldina.weatherapp.receiver.NetworkChangeReceiver
import com.goldina.weatherapp.utils.Constants
import com.goldina.weatherapp.utils.DataState
import com.goldina.weatherapp.utils.WeatherType
import com.goldina.weatherapp.utils.network.Network
import com.goldina.weatherapp.utils.network.OnNetworkListener
import com.goldina.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnNetworkListener {

    private var networkReceiver: NetworkChangeReceiver? = null
    private lateinit var binding:ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var snack: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvCityName.text =getString(R.string.city_name)
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

        binding.btnForecast.setOnClickListener {
            intent = Intent(applicationContext, ForecastActivity::class.java)
            startActivity(intent)
        }

        networkReceiver = NetworkChangeReceiver(this)
        refreshLayout()
    }
    private fun displayError(message: String?){
        if (message != null){
           showSnackBar(message)
        }else{
            showSnackBar(getString(R.string.issues_message))
        }
    }

    private fun refreshLayout() {
        binding.refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(applicationContext,R.color.colorPrimary),
            ContextCompat.getColor(applicationContext,R.color.colorPrimaryDark))
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

    private fun loadDataWeather() {
        viewModel.getCurrentWeatherData(
            Constants.LAT,
            Constants.LON,
            Constants.API_KEY,
            Constants.UNITS)
        viewModel.respCurWeather.observe(this) {response->
           run{
               when(response){
                   is DataState.Success<Any> -> {
                       displayProgressBar(false)
                      setData(response.data as ResponseCurrentDate)
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
    private fun setData(currentWeather: ResponseCurrentDate) {
        val dayNameFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        val timeCheck = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.apply {
            refreshLayout.isRefreshing=false
            val date=Date()
            tvDate.text = getString(R.string.date,dayNameFormat.format(date), dateFormat.format(date))
            tvTime.text =timeCheck.format(date)
            tvTemperature.text = getString(R.string.temp,currentWeather.main.temp.toInt())
            tvDescription.text = currentWeather.weather[0].description
            tvFeelsLike.text = getString(R.string.feels_like,currentWeather.main.feels_like.toInt())
            layoutDetailed.tvHumidity.text = getString(R.string.percent,currentWeather.main.humidity)
            layoutDetailed.tvPressure.text =  getString(R.string.pressure,currentWeather.main.pressure)
            layoutDetailed.tvWind.text = getString(R.string.wind,currentWeather.wind.speed.toInt())
            val iconId=currentWeather.weather[0].icon
            ivIconWeather.setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    WeatherType.fromWMO(iconId).iconRes))

            btnDetails.setOnClickListener {
                val intent = Intent( applicationContext, DetailsActivity::class.java)
                val dataBundle = Bundle()
                dataBundle.putSerializable(Constants.DETAIL_ITEM, currentWeather)
                intent.putExtras(dataBundle)
                startActivity(intent)
            }
        }
    }
    private fun displayProgressBar(isDisplay: Boolean){
        if(isDisplay){
            binding.progressBar.visibility = View.VISIBLE
            binding.groupData.visibility = View.INVISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.groupData.visibility = View.VISIBLE
        }
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

    @Suppress("DEPRECATION")
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